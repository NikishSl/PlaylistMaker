package com.practicum.playlistmaker

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.PlaylistEntity
import com.practicum.playlistmaker.db.PlaylistTrackEntity
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

    private val _tracksOpen = MutableLiveData<List<PlaylistTrackEntity>>()
    val tracksOpen: LiveData<List<PlaylistTrackEntity>>
        get() = _tracksOpen

    fun loadPlaylist(playlistId: Long) {
        viewModelScope.launch {
            val loadedPlaylist = playlistInteractor.getPlaylistById(playlistId)
            _playlist.postValue(loadedPlaylist)

            val trackIds = playlistRepository.getPlaylistById(playlistId)?.trackIds?.split(",")?.mapNotNull { it.trim().toIntOrNull() } ?: emptyList()
            val tracks = playlistRepository.getTracksForPlaylist(trackIds)
            val totalDurationMillis = tracks.sumOf { it.trackTimeMillis }
            val formattedDuration = formatDurationMinuteWord(totalDurationMillis)
            _formattedDuration.postValue(formattedDuration)
            _tracksOpen.postValue(tracks)
        }
    }

    fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int) {
        viewModelScope.launch {
            playlistRepository.deleteTrackFromPlaylist(playlistId, trackId)
            loadPlaylist(playlistId)
        }
    }

    fun sharePlaylist(activity: PlaylistOpenFragment, playlist: PlaylistEntity, tracks: List<PlaylistTrackEntity>) {
        if (tracks.isNullOrEmpty()) {
            Toast.makeText(activity.context, "В этом плейлисте нет списка треков, которым можно поделиться", Toast.LENGTH_SHORT).show()
        } else {
            val shareText = buildShareText(playlist, tracks)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            activity.startActivity(Intent.createChooser(shareIntent, "Поделитесь плейлистом"))
        }
    }

    private fun buildShareText(playlist: PlaylistEntity, tracks: List<PlaylistTrackEntity>): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("${playlist.name}\n")
        stringBuilder.append("${playlist.description}\n")
        stringBuilder.append("${changeTextTrackWithNumb(tracks.size)}\n")
        tracks.forEachIndexed { index, track ->
            stringBuilder.append("${index + 1}. ${track.artistName} - ${track.trackName} (${timeFormat.format(track.trackTimeMillis)})\n")
        }
        return stringBuilder.toString().trim()
    }
}