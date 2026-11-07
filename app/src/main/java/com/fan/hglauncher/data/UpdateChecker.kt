package com.fan.hglauncher.data

import com.fan.hglauncher.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * 更新检查器。
 *
 * 在 app/build.gradle.kts 里填好 UPDATE_MANIFEST_URL 后即可拉取真实清单：
 * { "version": "0.2.0", "url": "https://.../hg-launcher.apk", "notes": "..." }
 * 未配置时返回「已是最新」，UI 不阻塞。
 */
object UpdateChecker {

    suspend fun check(currentVersion: String): UpdateInfo = withContext(Dispatchers.IO) {
        val manifest = BuildConfig.UPDATE_MANIFEST_URL
        if (manifest.isBlank()) {
            return@withContext UpdateInfo(false, currentVersion)
        }
        runCatching {
            val conn = (URL(manifest).openConnection() as HttpURLConnection).apply {
                connectTimeout = 6000
                readTimeout = 6000
                requestMethod = "GET"
                setRequestProperty("Accept", "application/json")
            }
            try {
                val body = conn.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(body)
                val latest = json.optString("version")
                val url = json.optString("url").ifBlank { null }
                val notes = json.optString("notes").ifBlank { null }
                UpdateInfo(
                    hasUpdate = latest.isNotBlank() && latest != currentVersion,
                    latestVersion = latest.ifBlank { currentVersion },
                    apkUrl = url,
                    notes = notes
                )
            } finally {
                conn.disconnect()
            }
        }.getOrElse {
            UpdateInfo(false, currentVersion)
        }
    }
}
