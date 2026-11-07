package com.fan.hglauncher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fan.hglauncher.ui.theme.Amber
import com.fan.hglauncher.ui.theme.Mono
import com.fan.hglauncher.ui.theme.MutedDim
import com.fan.hglauncher.ui.theme.TextMain
import kotlinx.coroutines.delay
import kotlin.random.Random

private val bootLines = listOf(
    "初始化运行时…",
    "校验启动器完整性…",
    "加载游戏索引 04 …",
    "连接更新通道…",
    "就绪"
)

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        var p = 0f
        while (p < 1f) {
            delay(110L)
            p = (p + 0.06f + Random.nextFloat() * 0.05f).coerceAtMost(1f)
            progress = p
        }
        delay(420L)
        onFinished()
    }

    AmbientBackground {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                HexMark(diameter = 104.dp, color = Amber.copy(alpha = 0.14f), solid = true)
                HexMark(diameter = 62.dp, color = Amber)
            }
            Spacer(Modifier.height(28.dp))
            Text(
                "HG LAUNCHER",
                fontSize = 21.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 7.sp,
                color = TextMain
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "HYPERGRYPH FAN CLIENT",
                fontSize = 11.sp,
                letterSpacing = 2.4.sp,
                color = MutedDim,
                fontFamily = Mono
            )
            Spacer(Modifier.height(36.dp))

            Box(
                Modifier
                    .width(196.dp)
                    .height(3.dp)
                    .background(Color(0xFF20242B))
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(progress)
                        .height(3.dp)
                        .background(Amber)
                )
            }
            Spacer(Modifier.height(12.dp))
            val idx = (progress * bootLines.size).toInt().coerceIn(0, bootLines.size - 1)
            Text(
                bootLines[idx],
                fontSize = 10.5.sp,
                color = MutedDim,
                letterSpacing = 1.6.sp,
                fontFamily = Mono
            )
        }

        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "非官方同人作品 · 与鹰角网络无隶属关系",
                fontSize = 10.sp,
                color = Color(0xFF4B515A)
            )
            Spacer(Modifier.height(6.dp))
            Text("BUILD 0.1.1", fontSize = 10.sp, color = Color(0xFF4B515A), fontFamily = Mono)
        }
    }
}
