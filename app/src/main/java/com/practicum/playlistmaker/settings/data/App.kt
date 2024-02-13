package com.practicum.playlistmaker.settings.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.main.di.appModule
import com.practicum.playlistmaker.settings.domain.SwitchThemeUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    lateinit var switchThemeUseCase: SwitchThemeUseCase
        private set

    private val PREF_NAME = "ThemePrefs"
    private val KEY_DARK_THEME = "darkTheme"
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }

        val settingsRepository = SettingsRepository(getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE))
        switchThemeUseCase = SwitchThemeUseCase(settingsRepository)

        loadTheme()
        applyTheme()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        saveTheme()
        applyTheme()
    }

    private fun applyTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
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