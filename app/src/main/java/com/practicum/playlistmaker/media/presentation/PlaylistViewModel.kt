package com.practicum.playlistmaker.media.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.PlaylistEntity
import com.practicum.playlistmaker.media.createPlaylist.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {
    private val _playlists = MutableLiveData<List<PlaylistEntity>>()
    val playlists: LiveData<List<PlaylistEntity>> get() = _playlists

    fun loadPlaylists() {
        viewModelScope.launch {
            _playlists.value = playlistInteractor.getAllPlaylists()
        }
    }
}