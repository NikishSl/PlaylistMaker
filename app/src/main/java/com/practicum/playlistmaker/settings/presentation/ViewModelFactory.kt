package com.practicum.playlistmaker.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.settings.domain.SwitchThemeUseCase

class ViewModelFactory(private val switchThemeUseCase: SwitchThemeUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(switchThemeUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}