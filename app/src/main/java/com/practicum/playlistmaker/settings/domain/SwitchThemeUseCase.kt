package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.settings.data.SettingsRepository

class SwitchThemeUseCase(private val repository: SettingsRepository) {
    fun execute(darkThemeEnabled: Boolean) {
        repository.setDarkTheme(darkThemeEnabled)
    }

    fun isDarkThemeEnabled(): Boolean {
        return repository.isDarkThemeEnabled()
    }
}