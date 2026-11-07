# HG Launcher · 鹰角网络启动器（同人练习项目）

> ⚠️ 非官方同人作品，与鹰角网络（Hypergryph）无任何隶属关系。
> 项目不包含、不调用、不破解任何官方接口，仅作为 Android + Compose 的 UI/架构练习。

一个「鹰角风」的 Android 启动器：暗色工业面板、琥珀高亮、切角卡片、等宽数字。
完整实现了 **启动屏 → 登录 → 主页（游戏库 / 下载队列 / 公告）→ 设置** 的闭环。

---

## 一、能跑起来的三样东西

| 交付物 | 路径 | 说明 |
| --- | --- | --- |
| 高保真界面原型 | [`preview/index.html`](preview/index.html) | 单文件 HTML，手机浏览器直接打开就能点，用来先看效果 |
| Android 工程源码 | [`hg-launcher/`](hg-launcher/) | Kotlin + Jetpack Compose，Android Studio 打开即可编译 |
| 本文档 | `README.md` | 编译方式、目录结构、扩展点 |

> 当前环境（手机端）没有 JDK / Android SDK，无法直接产出 APK，
> 需要在电脑上用 Android Studio 编译。步骤见第四节。

---

## 二、已实现功能

### 1. 启动屏
- 六边形品牌标识 + 进度条 + 分阶段状态文案（初始化 → 校验 → 加载索引 → 连接通道 → 就绪）
- 进度跑完后若有本地会话直接进主页，否则进登录页

### 2. 登录
- 账号 / 密码输入（自定义工业风输入框、光标为琥珀色、密码掩码）
- 协议勾选校验、按钮禁用态、加载态「登 录 中 …」
- 本地模拟校验：非空 + 密码 ≥ 6 位
- 游客模式一键进入
- 会话持久化到 SharedPreferences，杀进程重开仍然是登录态

### 3. 主页
- 顶部栏（品牌标识、检查更新、设置入口）
- 主视觉卡：NEW VERSION 标签、版本号、补丁体积、开始游戏
- 我的游戏：2 列切角卡片、NEW 角标、状态文案
- 下载队列：进度条动画、速度、剩余时间、开始/暂停/继续/重新下载
- 公告列表：HOT/活动/维护标签
- 底部导航（首页 / 游戏库 / 公告 / 我的）

### 4. 设置
- 仅 Wi-Fi 下载、启动时检查更新、启动前自检、隐藏其他账号游戏（全部持久化）
- 清除缓存、退出登录（退出后回到登录页）

### 5. 更新通道（预留真实接口）
`UpdateChecker` 会读取 `BuildConfig.UPDATE_MANIFEST_URL`：

```json
{
  "version": "0.2.0",
  "url": "https://your.host/hg-launcher.apk",
  "notes": "修复若干问题"
}
```

在 `app/build.gradle.kts` 里填地址即可启用真实检查；不填则提示「已是最新」。
超时 6s，失败静默降级，不阻塞 UI。

---

## 三、目录结构

```
hg-launcher/
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/fan/hglauncher/
│       │   ├── MainActivity.kt          # 入口 + 屏幕路由 + Toast
│       │   ├── LauncherViewModel.kt     # 状态中枢（账号/下载/设置/更新）
│       │   ├── data/
│       │   │   ├── Models.kt            # User / Game / NewsItem / DownloadTask / UpdateInfo
│       │   │   ├── LocalStore.kt        # SharedPreferences 封装
│       │   │   ├── AuthRepository.kt    # 账号（本地模拟，替换这里即可接后端）
│       │   │   ├── GameRepository.kt    # 静态演示数据
│       │   │   └── UpdateChecker.kt     # 更新清单拉取
│       │   └── ui/
│       │       ├── theme/Theme.kt       # 色板 + 字体 + Material3 主题
│       │       ├── Common.kt            # 六边形、切角面板、按钮、开关、底部栏…
│       │       ├── SplashScreen.kt
│       │       ├── LoginScreen.kt
│       │       ├── HomeScreen.kt
│       │       └── SettingsScreen.kt
│       └── res/                          # 主题、颜色、自适应图标（矢量六边形）
└── gradle/wrapper/gradle-wrapper.properties
```

设计语言：`#0A0B0D` 底 + `#FFC21A` 琥珀 + 切角面板（`CutCornerShape(topEnd, bottomStart)`）
+ 网格背景 + 等宽数字，和 `preview/index.html` 是同一套视觉。

---

## 四、编译方式

### 方式 A：Android Studio（推荐）
1. Android Studio Hedgehog 以上，JDK 17
2. `File → Open` 选择 `hg-launcher` 目录
3. 首次打开会提示补全 Gradle Wrapper，点同意（或项目自带的 `gradle/wrapper` 配置会自动下载 Gradle 8.7）
4. 连接手机 / 模拟器，点 ▶ Run
5. 出包：`Build → Build Bundle(s)/APK(s) → Build APK(s)`，产物在
   `app/build/outputs/apk/debug/app-debug.apk`

### 方式 B：命令行
```bash
cd hg-launcher
# 若没有 wrapper jar，先执行一次（需要本机已装 Gradle 8.x）
gradle wrapper --gradle-version 8.7

./gradlew :app:assembleDebug
# 产物：app/build/outputs/apk/debug/app-debug.apk
```

环境要求：JDK 17、Android SDK Platform 34、Build-Tools 34.x。

### 方式 C：GitHub Actions 云编译（不用电脑 ⭐）

工程里已经放好了流水线：`.github/workflows/build-apk.yml`

1. 在 GitHub 新建一个仓库
2. 把 `hg-launcher` 里的**全部文件**（包括 `.github` 目录）推上去：
   ```bash
   cd hg-launcher
   git init
   git add .
   git commit -m "init: HG Launcher"
   git branch -M main
   git remote add origin https://github.com/<你的用户名>/<仓库名>.git
   git push -u origin main
   ```
3. 打开仓库的 **Actions** 页签 → 等 `Build APK` 跑完（约 3~6 分钟）
4. 点进这次运行 → 页面底部 **Artifacts** → 下载 `hg-launcher-debug.zip`
5. 解压得到 `app-debug.apk`，传到手机安装即可

> 打 tag 还能自动发 Release：
> ```bash
> git tag v0.1.0 && git push origin v0.1.0
> ```
> 会额外在 Releases 里生成带 APK 的版本页。

也可以在仓库 Actions 页面点 **Run workflow** 手动触发。

---

## 五、二次开发指引

| 想做的事 | 改哪里 |
| --- | --- |
| 接真实账号体系 | `data/AuthRepository.kt` 的 `login()`，保持返回 `Result<User>` 即可 |
| 换成后端游戏列表 | `data/GameRepository.kt` 改成 suspend 接口 + Room 缓存 |
| 真实下载器 | `LauncherViewModel.startDownload()` 替换为 OkHttp/DownloadManager + 进度回调 |
| 接真实更新服务 | `app/build.gradle.kts` 的 `UPDATE_MANIFEST_URL` |
| 改主题色 | `ui/theme/Theme.kt` 顶部的色板 |
| 加新页面 | `MainActivity.kt` 的 `Screen` 枚举 + `when` 分支 |

---

## 六、说明

- 「开始游戏」已经接入真实启动：`PackageManager.getLaunchIntentForPackage(包名)`，
  检测到已安装就直接拉起游戏，未安装则提示。包名映射见 `data/GameRepository.kt`，
  Android 11+ 的包可见性声明在 `AndroidManifest.xml` 的 `<queries>` 里。
- 「打开公告」仍是 Toast 演示。
- 账号、下载、更新这三块都是**本地模拟 + 可替换接口**，没有对接任何官方服务，
  也不建议对接（涉及账号安全与用户协议）。
- 素材均为自绘矢量/纯 CSS 绘制，未使用官方 logo 与美术资源。
