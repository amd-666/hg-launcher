package com.fan.hglauncher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fan.hglauncher.Settings
import com.fan.hglauncher.ui.theme.Danger
import com.fan.hglauncher.ui.theme.LineSoft
import com.fan.hglauncher.ui.theme.Mono
import com.fan.hglauncher.ui.theme.Muted
import com.fan.hglauncher.ui.theme.MutedDim
import com.fan.hglauncher.ui.theme.TextMain

@Composable
fun SettingsScreen(
    settings: Settings,
    versionName: String,
    onBack: () -> Unit,
    onAutoCheck: (Boolean) -> Unit,
    onWifiOnly: (Boolean) -> Unit,
    onSelfCheck: (Boolean) -> Unit,
    onHideOther: (Boolean) -> Unit,
    onClearCache: () -> Unit,
    onLogout: () -> Unit
) {
    AmbientBackground {
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // 顶栏
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .size(32.dp)
                        .clip(CutCornerShape(topEnd = 8.dp, bottomStart = 8.dp))
                        .background(Color(0xFF13161B))
                        .border(1.dp, LineSoft)
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("←", fontSize = 14.sp, color = Color(0xFFB9C0C9))
                }
                Spacer(Modifier.width(12.dp))
                Text("设 置", fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 3.sp)
            }

            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                GroupTitle("DOWNLOAD / 下载")
                SetRow("存储路径", "/storage/emulated/0/HGLauncher")
                SetRow("并发下载数", "4")
                ToggleRow("仅 Wi-Fi 下载", null, settings.wifiOnly, onWifiOnly)

                GroupTitle("LAUNCHER / 启动器")
                ToggleRow("启动时检查更新", "自动拉取版本清单", settings.autoCheckUpdate, onAutoCheck)
                ToggleRow("游戏启动前自检", "校验完整性，修复缺失文件", settings.selfCheck, onSelfCheck)
                ToggleRow("隐藏非本账号游戏", null, settings.hideOtherGames, onHideOther)

                GroupTitle("ABOUT / 关于")
                SetRow("版本", versionName)
                SetRow("更新通道", "稳定版")

                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onClearCache() }
                        .padding(vertical = 14.dp)
                ) {
                    Text("清除本地缓存", fontSize = 12.5.sp, color = Danger)
                    Spacer(Modifier.height(1.dp))
                }
                Divider()

                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onLogout() }
                        .padding(vertical = 14.dp)
                ) {
                    Text("退出登录", fontSize = 12.5.sp, color = Danger)
                }
                Divider()

                Spacer(Modifier.height(30.dp))
                Text(
                    "HG LAUNCHER 为非官方同人作品，与鹰角网络无隶属关系。",
                    fontSize = 10.sp,
                    color = Color(0xFF454A52),
                    lineHeight = 16.sp
                )
                Spacer(Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun GroupTitle(text: String) {
    Text(
        text,
        fontSize = 9.5.sp,
        color = MutedDim,
        letterSpacing = 2.2.sp,
        fontFamily = Mono,
        modifier = Modifier.padding(top = 22.dp, bottom = 6.dp)
    )
}

@Composable
private fun Divider() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(LineSoft)
    )
}

@Composable
private fun SetRow(title: String, value: String, sub: String? = null) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 12.5.sp, color = TextMain)
            if (sub != null) {
                Spacer(Modifier.height(3.dp))
                Text(sub, fontSize = 9.5.sp, color = MutedDim)
            }
        }
        Text(value, fontSize = 11.sp, color = Muted, fontFamily = Mono)
    }
    Divider()
}

@Composable
private fun ToggleRow(
    title: String,
    sub: String?,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 12.5.sp, color = TextMain)
            if (sub != null) {
                Spacer(Modifier.height(3.dp))
                Text(sub, fontSize = 9.5.sp, color = MutedDim)
            }
        }
        Spacer(Modifier.width(12.dp))
        ToggleSwitch(checked = checked, onChange = onChange)
    }
    Divider()
}
