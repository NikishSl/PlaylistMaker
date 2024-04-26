package com.practicum.playlistmaker.media.createPlaylist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.db.PlaylistDao
import com.practicum.playlistmaker.db.PlaylistEntity
import com.practicum.playlistmaker.db.PlaylistTrackDao
import com.practicum.playlistmaker.db.PlaylistTrackEntity

class PlaylistRepository(private val playlistDao: PlaylistDao, private val playlistTrackDao: PlaylistTrackDao) {

    private val _trackAddedToPlaylistStatus = MutableLiveData<Boolean>()
    val trackAddedToPlaylistStatus: LiveData<Boolean> get() = _trackAddedToPlaylistStatus

    suspend fun insertOrUpdatePlaylist(playlist: PlaylistEntity) {
        playlistDao.insertOrUpdatePlaylist(playlist)
    }

    suspend fun getAllPlaylists(): List<PlaylistEntity> {
        return playlistDao.getAllPlaylists()
    }

    suspend fun getPlaylistById(id: Long): PlaylistEntity? {
        return playlistDao.getPlaylistById(id)
    }

    suspend fun insertTrackIntoPlaylist(track: PlaylistTrackEntity, playlistId: Long) {
        val playlist = playlistDao.getPlaylistById(playlistId)
        if (playlist != null) {
            val trackIds = playlist.trackIds.split(",").toMutableList()
            if (!trackIds.contains(track.trackId.toString())) {
                trackIds.add(track.trackId.toString())
                val updatedPlaylist = if (playlist.trackIds.isEmpty()) {
                    playlist.copy(trackIds = trackIds.joinToString(","), trackCount = 1)
                } else {
                    playlist.copy(trackIds = trackIds.joinToString(","), trackCount = playlist.trackCount + 1)
                }
                playlistDao.insertOrUpdatePlaylist(updatedPlaylist)
                playlistTrackDao.insertTrack(track)
                _trackAddedToPlaylistStatus.postValue(true)
            } else {
                _trackAddedToPlaylistStatus.postValue(false)
            }
        } else {
            _trackAddedToPlaylistStatus.postValue(false)
        }
    }

    suspend fun isTrackInPlaylist(playlistId: Long, trackId: Int): Boolean {
        val playlist = playlistDao.getPlaylistById(playlistId)
        return if (playlist != null) {
            val trackIds = playlist.trackIds.split(",").map { it.trim() }
            if (trackIds.isNotEmpty()) {
                trackIds.mapNotNull { it.toIntOrNull() }.contains(trackId)
            } else {
                false
            }
        } else {
            false
        }
    }

}
