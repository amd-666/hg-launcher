package com.fan.hglauncher.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fan.hglauncher.ui.theme.Amber
import com.fan.hglauncher.ui.theme.AmberDim
import com.fan.hglauncher.ui.theme.Bg
import com.fan.hglauncher.ui.theme.Line
import com.fan.hglauncher.ui.theme.LineSoft
import com.fan.hglauncher.ui.theme.Mono
import com.fan.hglauncher.ui.theme.Muted
import com.fan.hglauncher.ui.theme.MutedDim
import com.fan.hglauncher.ui.theme.Panel
import com.fan.hglauncher.ui.theme.TextMain
import kotlin.math.cos
import kotlin.math.sin

/** 全局背景：深色底 + 网格 + 右上琥珀光晕 */
@Composable
fun AmbientBackground(content: @Composable BoxScope.() -> Unit) {
    Box(Modifier.fillMaxSize().background(Bg)) {
        Canvas(Modifier.fillMaxSize()) {
            val step = 34.dp.toPx()
            val gridColor = Color(0x0DFFFFFF)
            var x = 0f
            while (x <= size.width) {
                drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), 1f)
                x += step
            }
            var y = 0f
            while (y <= size.height) {
                drawLine(gridColor, Offset(0f, y), Offset(size.width, y), 1f)
                y += step
            }
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Amber.copy(alpha = 0.12f), Color.Transparent),
                    center = Offset(size.width * 0.92f, 0f),
                    radius = size.width * 1.0f
                )
            )
        }
        content()
    }
}

/** 六边形品牌标记 */
@Composable
fun HexMark(
    diameter: Dp = 26.dp,
    color: Color = Amber,
    inner: Color = Color(0xFF0A0B0D),
    solid: Boolean = false
) {
    Canvas(Modifier.size(diameter)) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val r = size.minDimension / 2f

        fun hexPath(radius: Float): Path = Path().apply {
            for (i in 0 until 6) {
                val angle = Math.toRadians((-90 + 60 * i).toDouble())
                val px = cx + (radius * cos(angle)).toFloat()
                val py = cy + (radius * sin(angle)).toFloat()
                if (i == 0) moveTo(px, py) else lineTo(px, py)
            }
            close()
        }

        drawPath(hexPath(r), color)
        if (!solid) {
            drawPath(hexPath(r * 0.60f), inner)
            drawPath(hexPath(r * 0.34f), color)
        }
    }
}

/** 切角面板（右上 + 左下切角，对应 CSS clip-path） */
@Composable
fun CutPanel(
    modifier: Modifier = Modifier,
    corner: Dp = 16.dp,
    borderColor: Color = Line,
    brush: Brush = SolidColor(Panel),
    content: @Composable BoxScope.() -> Unit
) {
    val shape = CutCornerShape(topEnd = corner, bottomStart = corner)
    Box(
        modifier = modifier
            .clip(shape)
            .background(brush)
            .border(1.dp, borderColor, shape)
    ) {
        content()
    }
}

/** 主行动按钮 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 52.dp,
    fontSize: TextUnit = 15.sp,
    letterSpacing: TextUnit = 4.sp
) {
    val shape = CutCornerShape(topEnd = 14.dp, bottomStart = 14.dp)
    val bg: Brush =
        if (enabled) Brush.horizontalGradient(listOf(Amber, Color(0xFFFFD75E)))
        else SolidColor(Color(0xFF24282F))
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(bg)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (enabled) Color(0xFF131417) else Color(0xFF5A616B),
            fontSize = fontSize,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = letterSpacing
        )
    }
}

/** 描边小胶囊按钮 */
@Composable
fun GhostPill(text: String, onClick: () -> Unit) {
    val shape = CutCornerShape(topEnd = 7.dp, bottomStart = 7.dp)
    Box(
        Modifier
            .clip(shape)
            .background(Color(0xFF14171C))
            .border(1.dp, Line, shape)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, fontSize = 10.sp, color = Color(0xFFAEB5BE), letterSpacing = 1.4.sp)
    }
}

/** 区块标题 */
@Composable
fun SectionHeader(title: String, trailing: String = "") {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 26.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(width = 3.dp, height = 13.dp).background(Amber))
        Spacer(Modifier.width(9.dp))
        Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
        Spacer(Modifier.weight(1f))
        if (trailing.isNotEmpty()) {
            Text(trailing, fontSize = 10.sp, color = MutedDim, letterSpacing = 1.6.sp, fontFamily = Mono)
        }
    }
}

/** 工业风开关 */
@Composable
fun ToggleSwitch(checked: Boolean, onChange: (Boolean) -> Unit) {
    val track = if (checked) Amber.copy(alpha = 0.18f) else Color(0xFF22262D)
    val edge = if (checked) AmberDim else Color(0xFF333A44)
    Box(
        Modifier
            .size(width = 40.dp, height = 22.dp)
            .background(track)
            .border(1.dp, edge)
            .clickable { onChange(!checked) }
    ) {
        Box(
            Modifier
                .padding(start = if (checked) 20.dp else 2.dp, top = 2.dp)
                .size(16.dp)
                .background(if (checked) Amber else Color(0xFF6E757F))
        )
    }
}

/** 细进度条 */
@Composable
fun ProgressLine(progress: Float, modifier: Modifier = Modifier, height: Dp = 5.dp) {
    val p by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        label = "progress"
    )
    Box(
        modifier
            .fillMaxWidth()
            .height(height)
            .background(Color(0xFF22262D))
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(p)
                .background(Brush.horizontalGradient(listOf(Color(0xFFB88600), Amber)))
        )
    }
}

/** 顶部栏 */
@Composable
fun LauncherTopBar(onCheck: () -> Unit, onSettings: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Color(0xF014161A), Color(0x660A0B0D))))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HexMark(24.dp)
        Spacer(Modifier.width(11.dp))
        Column {
            Text("HG LAUNCHER", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 3.sp)
            Text("FAN CLIENT / 0.1.0", fontSize = 8.5.sp, color = MutedDim, letterSpacing = 1.4.sp, fontFamily = Mono)
        }
        Spacer(Modifier.weight(1f))
        IconSquare("⟳", onCheck)
        Spacer(Modifier.width(8.dp))
        IconSquare("⚙", onSettings)
    }
}

@Composable
fun IconSquare(glyph: String, onClick: () -> Unit) {
    val shape = CutCornerShape(topEnd = 8.dp, bottomStart = 8.dp)
    Box(
        Modifier
            .size(34.dp)
            .clip(shape)
            .background(Color(0xFF13161B))
            .border(1.dp, LineSoft, shape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(glyph, fontSize = 14.sp, color = Color(0xFFA9B0B9))
    }
}

/** 小标签 */
@Composable
fun TagChip(text: String, hot: Boolean = false) {
    val c = if (hot) Amber else Color(0xFF8C939C)
    val e = if (hot) AmberDim else Color(0xFF333A44)
    Box(
        Modifier
            .border(1.dp, e)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text, fontSize = 8.5.sp, color = c, letterSpacing = 1.sp)
    }
}

/** 底部导航 */
@Composable
fun LauncherBottomBar(
    items: List<Pair<String, String>>,
    selected: Int,
    onSelect: (Int) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Color(0xCC0C0D10), Color(0xFA0A0B0D))))
            .padding(top = 8.dp, bottom = 8.dp, start = 6.dp, end = 6.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items.forEachIndexed { index, (icon, label) ->
            val on = index == selected
            Column(
                Modifier
                    .weight(1f)
                    .clickable { onSelect(index) }
                    .padding(vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(icon, fontSize = 15.sp, color = if (on) Amber else Muted)
                Spacer(Modifier.height(5.dp))
                Text(
                    label,
                    fontSize = 9.5.sp,
                    color = if (on) Amber else Muted,
                    letterSpacing = 1.2.sp
                )
            }
        }
    }
}
