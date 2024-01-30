package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences

class SettingsRepository(private val sharedPreferences: SharedPreferences) {
    fun setDarkTheme(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("darkTheme", enabled).apply()
    }

    fun isDarkThemeEnabled(): Boolean {
        return sharedPreferences.getBoolean("darkTheme", false)
    }
}