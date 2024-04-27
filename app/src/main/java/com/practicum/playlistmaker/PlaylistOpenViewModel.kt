package com.practicum.playlistmaker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.PlaylistEntity
import com.practicum.playlistmaker.media.createPlaylist.data.PlaylistRepository
import com.practicum.playlistmaker.media.createPlaylist.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistOpenViewModel(private val playlistInteractor: PlaylistInteractor, private val playlistRepository: PlaylistRepository) : ViewModel() {
    private val _backButtonClickedPL = MutableLiveData<Unit>()
    val backButtonClickedPL: LiveData<Unit>
        get() = _backButtonClickedPL

    private val _playlist = MutableLiveData<PlaylistEntity?>()
    val playlist: MutableLiveData<PlaylistEntity?>
        get() = _playlist

    private val _formattedDuration = MutableLiveData<String>()
    val formattedDuration: LiveData<String>
        get() = _formattedDuration

    fun onBackButtonClickedPL() {
        _backButtonClickedPL.value = Unit
    }

    fun loadPlaylist(playlistId: Long) {
        viewModelScope.launch {
            val loadedPlaylist = playlistInteractor.getPlaylistById(playlistId)
            _playlist.postValue(loadedPlaylist)

            val trackIds = playlistRepository.getPlaylistById(playlistId)?.trackIds?.split(",")?.mapNotNull { it.trim().toIntOrNull() } ?: emptyList()
            val tracks = playlistRepository.getTracksForPlaylist(trackIds)
            val totalDurationMillis = tracks.sumOf { it.trackTimeMillis }
            val formattedDuration = formatDurationMinuteWord(totalDurationMillis)
            _formattedDuration.postValue(formattedDuration)
        }
    }
}