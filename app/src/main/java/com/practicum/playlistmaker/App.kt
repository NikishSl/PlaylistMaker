package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private val PREF_NAME = "ThemePrefs"
    private val KEY_DARK_THEME = "darkTheme"

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        loadTheme()
        applyTheme()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        saveTheme()
        applyTheme()
    }

    fun applyTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun saveTheme() {
        val prefs: SharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean(KEY_DARK_THEME, darkTheme)
        editor.apply()
    }

    private fun loadTheme() {
        val prefs: SharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        darkTheme = prefs.getBoolean(KEY_DARK_THEME, false)
    }
}