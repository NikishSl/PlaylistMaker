package com.practicum.playlistmaker.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.player.domain.AudioPlayerInteractor

class PlayerViewModelFactory(private val audioPlayerInteractor: AudioPlayerInteractor) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(audioPlayerInteractor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}