package com.fan.hglauncher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/** 品牌色板：暗色工业 + 琥珀高亮 */
val Bg = Color(0xFF0A0B0D)
val Bg2 = Color(0xFF0F1114)
val Panel = Color(0xFF14161A)
val Panel2 = Color(0xFF191C21)
val Line = Color(0xFF2A2F37)
val LineSoft = Color(0xFF1F232A)

val Amber = Color(0xFFFFC21A)
val AmberBright = Color(0xFFFFDD75)
val AmberDim = Color(0xFF8A6A10)

val TextMain = Color(0xFFEDEFF2)
val Muted = Color(0xFF8C939C)
val MutedDim = Color(0xFF5D646E)
val Danger = Color(0xFFFF5C5C)
val OkColor = Color(0xFF5FE08B)

val Mono = FontFamily.Monospace

private val HgColors = darkColorScheme(
    primary = Amber,
    onPrimary = Color(0xFF131417),
    primaryContainer = AmberDim,
    onPrimaryContainer = AmberBright,
    secondary = AmberBright,
    background = Bg,
    onBackground = TextMain,
    surface = Panel,
    onSurface = TextMain,
    surfaceVariant = Panel2,
    onSurfaceVariant = Muted,
    outline = Line,
    error = Danger,
    onError = Color(0xFF131417)
)

private val HgTypography = Typography(
    bodyLarge = TextStyle(fontSize = 15.sp, lineHeight = 22.sp, color = TextMain),
    bodyMedium = TextStyle(fontSize = 13.sp, lineHeight = 20.sp, color = TextMain),
    bodySmall = TextStyle(fontSize = 11.sp, lineHeight = 17.sp, color = Muted),
    titleLarge = TextStyle(fontSize = 27.sp, lineHeight = 34.sp, fontWeight = FontWeight.ExtraBold, color = TextMain),
    titleMedium = TextStyle(fontSize = 15.sp, lineHeight = 22.sp, fontWeight = FontWeight.Bold, color = TextMain),
    labelSmall = TextStyle(fontSize = 10.sp, lineHeight = 14.sp, letterSpacing = 1.6.sp, color = MutedDim)
)

@Composable
fun HgTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = HgColors,
        typography = HgTypography,
        content = content
    )
}
