package com.fan.hglauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fan.hglauncher.ui.AmbientBackground
import com.fan.hglauncher.ui.HomeScreen
import com.fan.hglauncher.ui.LoginScreen
import com.fan.hglauncher.ui.SettingsScreen
import com.fan.hglauncher.ui.SplashScreen
import com.fan.hglauncher.ui.theme.Amber
import com.fan.hglauncher.ui.theme.Bg
import com.fan.hglauncher.ui.theme.Line
import com.fan.hglauncher.ui.theme.Panel
import com.fan.hglauncher.ui.theme.TextMain
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

private enum class Screen { Splash, Login, Home, Settings }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            com.fan.hglauncher.ui.theme.HgTheme {
                val vm: LauncherViewModel = viewModel()
                LauncherRoot(vm)
            }
        }
    }
}

@Composable
private fun LauncherRoot(vm: LauncherViewModel) {
    val user by vm.user.collectAsStateWithLifecycleCompat()
    val download by vm.download.collectAsStateWithLifecycleCompat()
    val settings by vm.settings.collectAsStateWithLifecycleCompat()

    var screen by remember { mutableStateOf(Screen.Splash) }
    var loggingIn by remember { mutableStateOf(false) }
    var toastText by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        vm.toast.collect { toastText = it }
    }
    LaunchedEffect(toastText) {
        val t = toastText ?: return@LaunchedEffect
        delay(1900L)
        if (toastText == t) toastText = null
    }
    LaunchedEffect(user) {
        if (user != null && settings.autoCheckUpdate) vm.checkUpdate()
    }

    BackHandler(enabled = screen == Screen.Settings) { screen = Screen.Home }

    Box(
        Modifier
            .fillMaxSize()
            .background(Bg)
    ) {
        Crossfade(targetState = screen, animationSpec = tween(260), label = "screen") { s ->
            when (s) {
                Screen.Splash -> SplashScreen(
                    onFinished = {
                        screen = if (vm.user.value != null) Screen.Home else Screen.Login
                    }
                )

                Screen.Login -> LoginScreen(
                    loading = loggingIn,
                    onLogin = { account, pwd ->
                        loggingIn = true
                        vm.login(account, pwd,
                            onSuccess = {
                                loggingIn = false
                                screen = Screen.Home
                            },
                            onFailure = { loggingIn = false }
                        )
                    },
                    onGuest = { vm.guestLogin { screen = Screen.Home } },
                    onHelp = { vm.notify("账号体系为本地模拟，无需真实账号") }
                )

                Screen.Home -> AmbientBackground {
                    HomeScreen(
                        games = vm.games,
                        news = vm.news,
                        download = download,
                        onPlay = { vm.notify("正在启动「明日方舟」… 当前为演示环境") },
                        onGame = { game -> vm.notify("打开「" + game.title.replace("\n", "") + "」详情页（演示）") },
                        onToggleDownload = { vm.toggleDownload() },
                        onNews = { item -> vm.notify("打开公告：" + item.title) },
                        onCheckUpdate = { vm.checkUpdate() },
                        onSettings = { screen = Screen.Settings }
                    )
                }

                Screen.Settings -> SettingsScreen(
                    settings = settings,
                    versionName = BuildConfig.VERSION_NAME,
                    onBack = { screen = Screen.Home },
                    onAutoCheck = { vm.setAutoCheckUpdate(it) },
                    onWifiOnly = { vm.setWifiOnly(it) },
                    onSelfCheck = { vm.setSelfCheck(it) },
                    onHideOther = { vm.setHideOtherGames(it) },
                    onClearCache = { vm.clearCache() },
                    onLogout = {
                        vm.logout()
                        screen = Screen.Login
                    }
                )
            }
        }

        AnimatedVisibility(
            visible = toastText != null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 96.dp),
            enter = fadeIn(tween(180)) + slideInVertically(tween(220)) { it / 2 },
            exit = fadeOut(tween(160)) + slideOutVertically(tween(180)) { it / 2 }
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .background(Panel)
                    .border(1.dp, Line)
                    .padding(horizontal = 16.dp, vertical = 11.dp)
            ) {
                Text(
                    toastText ?: "",
                    color = TextMain,
                    fontSize = 11.5.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

/** 轻量替代 collectAsStateWithLifecycle（避免额外依赖 lifecycle-runtime-compose） */
@Composable
private fun <T> StateFlow<T>.collectAsStateWithLifecycleCompat(): State<T> = collectAsState()
