package com.practicum.playlistmaker.media.createPlaylist.domain

import com.practicum.playlistmaker.media.createPlaylist.data.PlaylistRepository
import com.practicum.playlistmaker.db.PlaylistEntity
import com.practicum.playlistmaker.db.PlaylistTrackEntity

class PlaylistInteractor(private val playlistRepository: PlaylistRepository) {
    suspend fun getAllPlaylists(): List<PlaylistEntity> {
        return playlistRepository.getAllPlaylists()
    }
    suspend fun insertTrackIntoPlaylist(track: PlaylistTrackEntity, playlistId: Long) {
        playlistRepository.insertTrackIntoPlaylist(track, playlistId)
    }

    suspend fun isTrackInPlaylist(playlistId: Long, trackId: Int): Boolean {
        return playlistRepository.isTrackInPlaylist(playlistId, trackId)
    }
}