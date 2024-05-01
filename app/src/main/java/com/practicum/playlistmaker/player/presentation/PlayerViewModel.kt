package com.practicum.playlistmaker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.PlaylistEntity
import com.practicum.playlistmaker.media.createPlaylist.domain.PlaylistInteractor
import com.practicum.playlistmaker.media.createPlaylist.data.PlaylistRepository
import com.practicum.playlistmaker.db.PlaylistTrackEntity
import com.practicum.playlistmaker.media.domain.FavoritesInteractor
import com.practicum.playlistmaker.player.domain.AudioPlayerInteractor
import com.practicum.playlistmaker.search.data.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class PlayerViewModel(
    val audioPlayerInteractor: AudioPlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    companion object {
        private const val TRACK_TIME_UPDATE_DELAY_MILLIS = 1000L
    }

    private val _track = MutableLiveData<Track>()
    val track: LiveData<Track>
        get() = _track
    private val _trackTime = MutableLiveData<Int>()
    val trackTime: LiveData<Int>
        get() = _trackTime
    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _playlists = MutableLiveData<List<PlaylistEntity>>()
    val playlists: LiveData<List<PlaylistEntity>> get() = _playlists

    private val _isTrackInPlaylist = MutableLiveData<Boolean>()
    val isTrackInPlaylist: LiveData<Boolean> get() = _isTrackInPlaylist

    private val _trackAddedToPlaylistStatus = MutableLiveData<Boolean>()
    val trackAddedToPlaylistStatus: LiveData<Boolean> get() = _trackAddedToPlaylistStatus

    private val _playlistTE = MutableLiveData<PlaylistTrackEntity?>()
    val playlistTE: LiveData<PlaylistTrackEntity?>
        get() = _playlistTE

    private var trackTimeJob: Job? = null

    fun setTrack(track: Track) {
        _track.value = track
        audioPlayerInteractor.setDataSource(track.previewUrl)
        checkIsFavorite(track)
    }

    fun getAllPlaylists(){
        viewModelScope.launch {
            _playlists.value = playlistInteractor.getAllPlaylists()
        }
    }

    fun insertTrackIntoPlaylist(track: PlaylistTrackEntity, playlistId: Long) {
        viewModelScope.launch {
            playlistRepository.insertTrackIntoPlaylist(track, playlistId)
            getAllPlaylists()
        }
    }

    fun checkTrackInPlaylist(playlistId: Long, trackId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isInPlaylist = playlistInteractor.isTrackInPlaylist(playlistId, trackId)
            callback(isInPlaylist)
        }
    }

    fun playOrPause() {
        if (audioPlayerInteractor.isPlaying()) {
            audioPlayerInteractor.pause()
        } else {
            audioPlayerInteractor.start()
        }
    }

    fun startTrackTimeUpdates() {
        trackTimeJob = viewModelScope.launch {
            while (true) {
                val currentTime = audioPlayerInteractor.getCurrentPosition()
                _trackTime.postValue(currentTime)
                delay(TRACK_TIME_UPDATE_DELAY_MILLIS)
            }
        }
    }

    fun onFavoriteClicked() {
        val currentTrack = _track.value ?: return
        viewModelScope.launch {
            if (_isFavorite.value == true) {
                favoritesInteractor.removeTrackFromFavorites(currentTrack)
            } else {
                favoritesInteractor.addTrackToFavorites(currentTrack)
            }
            _isFavorite.postValue(!(_isFavorite.value ?: false))
        }
    }

    private fun checkIsFavorite(track: Track) {
        viewModelScope.launch {
            val favoriteTracks = favoritesInteractor.getAllFavoriteTracks().firstOrNull()
            val isFavorite = favoriteTracks?.any { it.trackId == track.trackId } ?: false
            _isFavorite.postValue(isFavorite)
        }
    }

    fun stopTrackTimeUpdates() {
        trackTimeJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerInteractor.release()
        trackTimeJob?.cancel()
    }

    fun loadPlaylistTE(trackId: Int) {
        viewModelScope.launch {
            val loadedTrack = playlistRepository.getTrackById(trackId)
            _playlistTE.postValue(loadedTrack)

            loadedTrack?.let {
                if (!it.previewUrl.isNullOrEmpty()){
                    val favoriteTracks = favoritesInteractor.getAllFavoriteTracks().firstOrNull()
                    val isFavorite = favoriteTracks?.any { it.trackId == loadedTrack.trackId } ?: false
                    val track = convertToTrack(loadedTrack, isFavorite)
                    _track.postValue(track)
                    _isFavorite.postValue(isFavorite)

                    audioPlayerInteractor.setDataSource(it.previewUrl)
                    setTrack(track)
                }
            }
        }
    }

    private fun convertToTrack(playlistTrackEntity: PlaylistTrackEntity, isFavorite: Boolean): Track {
        return Track(
            playlistTrackEntity.trackName,
            playlistTrackEntity.artistName,
            playlistTrackEntity.trackTimeMillis,
            playlistTrackEntity.artworkUrl100,
            playlistTrackEntity.trackId,
            playlistTrackEntity.collectionName,
            playlistTrackEntity.releaseDate,
            playlistTrackEntity.primaryGenreName,
            playlistTrackEntity.country,
            playlistTrackEntity.previewUrl,
            isFavorite
        )
    }
}