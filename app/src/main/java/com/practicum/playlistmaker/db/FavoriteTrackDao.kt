package com.practicum.playlistmaker.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteTrackDao {
    @Insert
    suspend fun insertTrack(track: FavoriteTrackEntity)

    @Delete
    suspend fun deleteTrack(track: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_tracks")
    suspend fun getAllFavoriteTracks(): List<FavoriteTrackEntity>

    @Query("SELECT trackId FROM favorite_tracks")
    suspend fun getAllFavoriteTrackIds(): List<Int>
}