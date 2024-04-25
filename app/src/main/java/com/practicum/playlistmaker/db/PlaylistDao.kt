package com.practicum.playlistmaker.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist WHERE playlistId = :id")
    suspend fun getPlaylistById(id: Long): PlaylistEntity?

}