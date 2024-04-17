package com.practicum.playlistmaker

class PlaylistInteractor(private val playlistRepository: PlaylistRepository) {
    suspend fun getAllPlaylists(): List<PlaylistEntity> {
        return playlistRepository.getAllPlaylists()
    }
}