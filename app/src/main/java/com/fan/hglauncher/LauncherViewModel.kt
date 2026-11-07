package com.fan.hglauncher

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fan.hglauncher.data.AuthRepository
import com.fan.hglauncher.data.DownloadTask
import com.fan.hglauncher.data.Game
import com.fan.hglauncher.data.GameRepository
import com.fan.hglauncher.data.LocalStore
import com.fan.hglauncher.data.UpdateChecker
import com.fan.hglauncher.data.User
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

data class Settings(
    val autoCheckUpdate: Boolean = true,
    val wifiOnly: Boolean = true,
    val selfCheck: Boolean = true,
    val hideOtherGames: Boolean = false
)

class LauncherViewModel(app: Application) : AndroidViewModel(app) {

    private val store = LocalStore(app)
    private val auth = AuthRepository()

    private val _user = MutableStateFlow(store.user)
    val user = _user.asStateFlow()

    /** 启动时做一次真实安装检测，游戏卡片状态以本机为准 */
    val games: List<Game> = GameRepository.games.map { g ->
        val installed = isInstalled(g.packageName)
        g.copy(
            installed = installed,
            status = when {
                g.id == "endfield" -> "预约中"
                installed -> "已安装 · 就绪"
                else -> "未安装"
            }
        )
    }
    val news = GameRepository.news

    private val _download = MutableStateFlow(DownloadTask())
    val download = _download.asStateFlow()

    private val _checking = MutableStateFlow(false)
    val checking = _checking.asStateFlow()

    private val _settings = MutableStateFlow(
        Settings(
            autoCheckUpdate = store.autoCheckUpdate,
            wifiOnly = store.wifiOnly,
            selfCheck = store.selfCheckBeforeLaunch,
            hideOtherGames = store.hideOtherGames
        )
    )
    val settings = _settings.asStateFlow()

    private val _toast = MutableSharedFlow<String>(extraBufferCapacity = 8)
    val toast = _toast.asSharedFlow()

    private var downloadJob: Job? = null

    fun notify(msg: String) {
        _toast.tryEmit(msg)
    }

    // ---------- 账号 ----------

    fun login(
        account: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit = {}
    ) {
        viewModelScope.launch {
            auth.login(account, password)
                .onSuccess { u ->
                    _user.value = u
                    store.user = u
                    notify("登录成功，欢迎回来，${u.nick}")
                    onSuccess()
                }
                .onFailure {
                    notify(it.message ?: "登录失败，请重试")
                    onFailure()
                }
        }
    }

    fun guestLogin(onSuccess: () -> Unit) {
        val u: User = auth.guest()
        _user.value = u
        notify("已进入游客模式（本地模拟）")
        onSuccess()
    }

    fun logout() {
        store.user = null
        _user.value = null
        notify("已退出登录")
    }

    // ---------- 启动游戏 ----------

    private fun isInstalled(packageName: String): Boolean {
        if (packageName.isBlank()) return false
        return getApplication<Application>().packageManager
            .getLaunchIntentForPackage(packageName) != null
    }

    /**
     * 真正的启动逻辑：检测包名 -> 拉起 launcher Activity。
     * 未安装时给出提示（本机没有对应游戏时也走这条分支）。
     */
    fun launchGame(game: Game) {
        val ctx = getApplication<Application>()
        val name = game.title.replace("\n", "")

        if (game.packageName.isBlank()) {
            notify("「$name」暂不支持启动")
            return
        }

        val intent = ctx.packageManager.getLaunchIntentForPackage(game.packageName)
        if (intent == null) {
            notify("未检测到《$name》，请先安装或前往应用商店")
            return
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        runCatching { ctx.startActivity(intent) }
            .onSuccess { notify("正在启动《$name》…") }
            .onFailure { notify("启动失败：${it.message ?: "未知错误"}") }
    }

    // ---------- 下载 ----------

    fun toggleDownload() {
        val cur = _download.value
        when {
            cur.finished -> {
                _download.value = DownloadTask()
                notify("重新开始下载资源包")
                startDownload()
            }
            cur.running -> {
                downloadJob?.cancel()
                downloadJob = null
                _download.value = cur.copy(running = false, speedMb = 0f)
                notify("已暂停下载")
            }
            else -> {
                startDownload()
                notify("开始下载 · 并发 4")
            }
        }
    }

    private fun startDownload() {
        downloadJob?.cancel()
        downloadJob = viewModelScope.launch {
            _download.value = _download.value.copy(running = true, finished = false)
            while (true) {
                delay(620L)
                val cur = _download.value
                val speed = 6f + Random.nextFloat() * 9f
                val done = (cur.doneMb + speed * 0.62f).coerceAtMost(cur.totalMb)
                val finished = done >= cur.totalMb
                _download.value = cur.copy(
                    doneMb = done,
                    speedMb = if (finished) 0f else speed,
                    running = !finished,
                    finished = finished
                )
                if (finished) {
                    notify("「${cur.title}」下载完成")
                    break
                }
            }
        }
    }

    // ---------- 更新 ----------

    fun checkUpdate() {
        if (_checking.value) return
        viewModelScope.launch {
            _checking.value = true
            notify("检查更新中…")
            val info = UpdateChecker.check(BuildConfig.VERSION_NAME)
            _checking.value = false
            if (info.hasUpdate) {
                notify("发现新版本 ${info.latestVersion}")
            } else {
                notify("已是最新版本 ${BuildConfig.VERSION_NAME}")
            }
        }
    }

    // ---------- 设置 ----------

    fun setAutoCheckUpdate(v: Boolean) {
        store.autoCheckUpdate = v
        _settings.value = _settings.value.copy(autoCheckUpdate = v)
        notify(if (v) "启动时自动检查更新：开" else "启动时自动检查更新：关")
    }

    fun setWifiOnly(v: Boolean) {
        store.wifiOnly = v
        _settings.value = _settings.value.copy(wifiOnly = v)
        notify(if (v) "仅 Wi-Fi 下载：开" else "仅 Wi-Fi 下载：关")
    }

    fun setSelfCheck(v: Boolean) {
        store.selfCheckBeforeLaunch = v
        _settings.value = _settings.value.copy(selfCheck = v)
        notify(if (v) "启动前自检：开" else "启动前自检：关")
    }

    fun setHideOtherGames(v: Boolean) {
        store.hideOtherGames = v
        _settings.value = _settings.value.copy(hideOtherGames = v)
        notify(if (v) "已隐藏其他账号游戏" else "已显示全部游戏")
    }

    fun clearCache() {
        store.clearCache()
        notify("本地缓存已清理")
    }
}
