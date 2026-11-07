package com.fan.hglauncher.data

import java.util.Locale

data class User(
    val id: String,
    val nick: String
)

data class Game(
    val id: String,
    val title: String,
    val status: String,
    val glyph: String,
    val installed: Boolean,
    val isNew: Boolean = false
)

data class NewsItem(
    val id: String,
    val tag: String,
    val title: String,
    val date: String,
    val hot: Boolean = false
)

data class DownloadTask(
    val title: String = "明日方舟 · 资源包 A",
    val totalMb: Float = 2457.6f,
    val doneMb: Float = 0f,
    val speedMb: Float = 0f,
    val running: Boolean = false,
    val finished: Boolean = false
) {
    val progress: Float
        get() = if (totalMb <= 0f) 0f else (doneMb / totalMb).coerceIn(0f, 1f)

    val pctText: String
        get() = String.format(Locale.ROOT, "%.1f%%", progress * 100f)

    val stateText: String
        get() = when {
            finished -> "已完成"
            running -> "下载中"
            doneMb > 0f -> "已暂停"
            else -> "等待中"
        }

    val subText: String
        get() = "$stateText · 已下载 ${fmtMb(doneMb)} / ${fmtMb(totalMb)}"

    val speedText: String
        get() = if (running) String.format(Locale.ROOT, "%.1f MB/s", speedMb) else "— MB/s"

    val etaText: String
        get() {
            if (!running || speedMb <= 0f) return "剩余 --:--"
            val remain = ((totalMb - doneMb) / speedMb).toInt()
            return String.format(Locale.ROOT, "剩余 %02d:%02d", remain / 60, remain % 60)
        }

    companion object {
        fun fmtMb(mb: Float): String =
            if (mb >= 1024f) String.format(Locale.ROOT, "%.1f GB", mb / 1024f)
            else String.format(Locale.ROOT, "%.0f MB", mb)
    }
}

data class UpdateInfo(
    val hasUpdate: Boolean,
    val latestVersion: String,
    val apkUrl: String? = null,
    val notes: String? = null
)
