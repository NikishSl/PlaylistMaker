package com.practicum.playlistmaker

class PlaylistRepository(private val playlistDao: PlaylistDao) {
    suspend fun insertOrUpdatePlaylist(playlist: PlaylistEntity) {
        playlistDao.insertOrUpdatePlaylist(playlist)
    }

}
