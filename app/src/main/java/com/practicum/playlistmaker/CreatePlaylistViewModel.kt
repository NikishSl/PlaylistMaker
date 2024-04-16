package com.practicum.playlistmaker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val playlistRepository: PlaylistRepository) : ViewModel() {
    private val _backButtonClicked = MutableLiveData<Unit>()
    val backButtonClicked: LiveData<Unit>
        get() = _backButtonClicked

    private val _isCreateButtonEnabled = MutableLiveData<Boolean>()
    val isCreateButtonEnabled: LiveData<Boolean>
        get() = _isCreateButtonEnabled

    init {
        _isCreateButtonEnabled.value = false
    }

    fun onBackButtonClicked() {
        _backButtonClicked.value = Unit
    }

    fun updateCreateButtonState(isEnabled: Boolean) {
        _isCreateButtonEnabled.value = isEnabled
    }

    fun savePlaylist(playlist: PlaylistEntity) {
        viewModelScope.launch {
            playlistRepository.insertOrUpdatePlaylist(playlist)
        }
    }
}