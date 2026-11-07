package com.fan.hglauncher.data

import android.content.Context

/**
 * 轻量本地存储：会话 + 启动器偏好。
 * 真实项目可替换为 DataStore / EncryptedSharedPreferences。
 */
class LocalStore(context: Context) {

    private val sp = context.getSharedPreferences("hg_launcher", Context.MODE_PRIVATE)

    var user: User?
        get() {
            val id = sp.getString(KEY_USER_ID, null) ?: return null
            val nick = sp.getString(KEY_USER_NICK, "博士") ?: "博士"
            return User(id, nick)
        }
        set(value) {
            sp.edit().apply {
                if (value == null) {
                    remove(KEY_USER_ID)
                    remove(KEY_USER_NICK)
                } else {
                    putString(KEY_USER_ID, value.id)
                    putString(KEY_USER_NICK, value.nick)
                }
            }.apply()
        }

    var autoCheckUpdate: Boolean
        get() = sp.getBoolean(KEY_AUTO_UPDATE, true)
        set(v) = sp.edit().putBoolean(KEY_AUTO_UPDATE, v).apply()

    var wifiOnly: Boolean
        get() = sp.getBoolean(KEY_WIFI_ONLY, true)
        set(v) = sp.edit().putBoolean(KEY_WIFI_ONLY, v).apply()

    var selfCheckBeforeLaunch: Boolean
        get() = sp.getBoolean(KEY_SELF_CHECK, true)
        set(v) = sp.edit().putBoolean(KEY_SELF_CHECK, v).apply()

    var hideOtherGames: Boolean
        get() = sp.getBoolean(KEY_HIDE_OTHER, false)
        set(v) = sp.edit().putBoolean(KEY_HIDE_OTHER, v).apply()

    fun clearCache() {
        sp.edit().remove(KEY_CACHE_SIZE).apply()
    }

    private companion object {
        const val KEY_USER_ID = "user_id"
        const val KEY_USER_NICK = "user_nick"
        const val KEY_AUTO_UPDATE = "auto_check_update"
        const val KEY_WIFI_ONLY = "wifi_only"
        const val KEY_SELF_CHECK = "self_check"
        const val KEY_HIDE_OTHER = "hide_other_games"
        const val KEY_CACHE_SIZE = "cache_size"
    }
}
