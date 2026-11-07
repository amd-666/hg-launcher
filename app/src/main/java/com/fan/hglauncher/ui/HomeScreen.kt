package com.fan.hglauncher.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fan.hglauncher.data.DownloadTask
import com.fan.hglauncher.data.Game
import com.fan.hglauncher.data.NewsItem
import com.fan.hglauncher.ui.theme.Amber
import com.fan.hglauncher.ui.theme.AmberDim
import com.fan.hglauncher.ui.theme.Line
import com.fan.hglauncher.ui.theme.LineSoft
import com.fan.hglauncher.ui.theme.Mono
import com.fan.hglauncher.ui.theme.Muted
import com.fan.hglauncher.ui.theme.MutedDim
import com.fan.hglauncher.ui.theme.TextMain

@Composable
fun HomeScreen(
    games: List<Game>,
    news: List<NewsItem>,
    download: DownloadTask,
    onPlay: () -> Unit,
    onGame: (Game) -> Unit,
    onToggleDownload: () -> Unit,
    onNews: (NewsItem) -> Unit,
    onCheckUpdate: () -> Unit,
    onSettings: () -> Unit
) {
    var tab by remember { mutableStateOf(0) }

    Column(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        LauncherTopBar(onCheck = onCheckUpdate, onSettings = onSettings)

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            HeroSection(onPlay)

            SectionHeader("我的游戏", "04 GAMES")
            GameGrid(games, onGame)

            SectionHeader("下载队列", "01 TASK")
            DownloadCard(download, onToggleDownload)

            SectionHeader("公告", "03 ITEMS")
            NewsList(news, onNews)

            Spacer(Modifier.height(28.dp))
        }

        Box(Modifier.navigationBarsPadding()) {
            LauncherBottomBar(
                items = listOf(
                    "▣" to "首页",
                    "▤" to "游戏库",
                    "◫" to "公告",
                    "◎" to "我的"
                ),
                selected = tab,
                onSelect = { tab = it }
            )
        }
    }
}

@Composable
private fun HeroSection(onPlay: () -> Unit) {
    val shape = CutCornerShape(topEnd = 16.dp, bottomStart = 16.dp)
    Box(
        Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Brush.linearGradient(listOf(Color(0xFF1A1D23), Color(0xFF101216))))
            .border(1.dp, Line, shape)
    ) {
        Canvas(Modifier.matchParentSize()) {
            drawRect(
                Brush.radialGradient(
                    colors = listOf(Amber.copy(alpha = 0.16f), Color.Transparent),
                    center = Offset(size.width, 0f),
                    radius = size.width * 1.1f
                )
            )
            val step = 26.dp.toPx()
            var x = -size.height
            while (x < size.width) {
                drawLine(
                    color = Color(0x0AFFFFFF),
                    start = Offset(x, 0f),
                    end = Offset(x + size.height, size.height),
                    strokeWidth = 1f
                )
                x += step
            }
        }

        Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 18.dp)) {
            Box(
                Modifier
                    .background(Amber)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    "NEW VERSION",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    color = Color(0xFF0E1013)
                )
            }
            Spacer(Modifier.height(14.dp))
            Text(
                "明日方舟",
                fontSize = 29.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp,
                color = TextMain
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "「熔火与冰原」版本更新 · 全新六星干员「凛霜」登场",
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = Color(0xFFA7AEB8)
            )
            Spacer(Modifier.height(14.dp))
            Row {
                Text("VER ", fontSize = 10.sp, color = MutedDim, fontFamily = Mono)
                Text("2.6.01", fontSize = 10.sp, color = Amber, fontFamily = Mono)
                Spacer(Modifier.width(14.dp))
                Text("PATCH ", fontSize = 10.sp, color = MutedDim, fontFamily = Mono)
                Text("2.4 GB", fontSize = 10.sp, color = Amber, fontFamily = Mono)
                Spacer(Modifier.width(14.dp))
                Text("已是最新", fontSize = 10.sp, color = MutedDim)
            }
            Spacer(Modifier.height(18.dp))
            PrimaryButton(
                text = "▶   开始游戏",
                onClick = onPlay,
                height = 46.dp,
                fontSize = 13.5.sp,
                letterSpacing = 3.sp
            )
        }
    }
}

@Composable
private fun GameGrid(games: List<Game>, onGame: (Game) -> Unit) {
    games.chunked(2).forEach { row ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            row.forEach { game ->
                GameCard(game, Modifier.weight(1f), onGame)
            }
            if (row.size == 1) Spacer(Modifier.weight(1f))
        }
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun GameCard(game: Game, modifier: Modifier = Modifier, onClick: (Game) -> Unit) {
    val shape = CutCornerShape(topEnd = 12.dp, bottomStart = 12.dp)
    Box(
        modifier
            .height(92.dp)
            .clip(shape)
            .background(Brush.linearGradient(listOf(Color(0xFF171A1F), Color(0xFF101216))))
            .border(1.dp, LineSoft, shape)
            .clickable { onClick(game) }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(start = 13.dp, end = 13.dp, top = 12.dp, bottom = 12.dp)
        ) {
            Text(
                game.title,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 17.sp,
                color = TextMain,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                game.status,
                fontSize = 9.sp,
                color = MutedDim,
                fontFamily = Mono,
                letterSpacing = 0.6.sp
            )
        }
        Text(
            game.glyph,
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            color = Color.White.copy(alpha = 0.06f),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 9.dp, bottom = 2.dp)
        )
        if (game.isNew) {
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .background(Amber)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    "NEW",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = Color(0xFF121316)
                )
            }
        }
    }
}

@Composable
private fun DownloadCard(task: DownloadTask, onToggle: () -> Unit) {
    CutPanel(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(15.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(34.dp)
                        .clip(CutCornerShape(topEnd = 9.dp, bottomStart = 9.dp))
                        .background(Brush.linearGradient(listOf(Color(0xFF2A2110), Color(0xFF171A1F))))
                        .border(1.dp, AmberDim),
                    contentAlignment = Alignment.Center
                ) {
                    Text("R", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Amber)
                }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        task.title,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        task.subText,
                        fontSize = 9.5.sp,
                        color = Color(0xFF6A717B),
                        fontFamily = Mono
                    )
                }
                Spacer(Modifier.width(8.dp))
                GhostPill(
                    text = when {
                        task.finished -> "重新"
                        task.running -> "暂停"
                        task.doneMb > 0f -> "继续"
                        else -> "开始"
                    },
                    onClick = onToggle
                )
            }
            Spacer(Modifier.height(12.dp))
            ProgressLine(task.progress)
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "PROGRESS ${task.pctText}",
                    fontSize = 10.sp,
                    color = MutedDim,
                    fontFamily = Mono
                )
                Text(task.speedText, fontSize = 10.sp, color = MutedDim, fontFamily = Mono)
                Text(task.etaText, fontSize = 10.sp, color = MutedDim, fontFamily = Mono)
            }
        }
    }
}

@Composable
private fun NewsList(news: List<NewsItem>, onClick: (NewsItem) -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        news.forEach { item ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { onClick(item) }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TagChip(item.tag, hot = item.hot)
                Spacer(Modifier.width(10.dp))
                Text(
                    item.title,
                    fontSize = 12.sp,
                    color = Color(0xFFB9C0C9),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(10.dp))
                Text(item.date, fontSize = 9.5.sp, color = MutedDim, fontFamily = Mono)
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(LineSoft)
            )
        }
    }
}
