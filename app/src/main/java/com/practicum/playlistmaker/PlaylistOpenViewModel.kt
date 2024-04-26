package com.practicum.playlistmaker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.PlaylistEntity
import com.practicum.playlistmaker.media.createPlaylist.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistOpenViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {
    private val _backButtonClickedPL = MutableLiveData<Unit>()
    val backButtonClickedPL: LiveData<Unit>
        get() = _backButtonClickedPL

    private val _playlist = MutableLiveData<PlaylistEntity?>()
    val playlist: MutableLiveData<PlaylistEntity?>
        get() = _playlist

    fun onBackButtonClickedPL() {
        _backButtonClickedPL.value = Unit
    }

    fun loadPlaylist(playlistId: Long) {
        viewModelScope.launch {
            val loadedPlaylist = playlistInteractor.getPlaylistById(playlistId)
            _playlist.postValue(loadedPlaylist)
        }
    }
}