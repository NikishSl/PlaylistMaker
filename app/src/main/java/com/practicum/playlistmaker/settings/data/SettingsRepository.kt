package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences

class SettingsRepository(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val DARK_THEME_KEY = "darkTheme"
    }
    fun setDarkTheme(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(DARK_THEME_KEY, enabled).apply()
    }

    fun isDarkThemeEnabled(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME_KEY, false)
    }
}