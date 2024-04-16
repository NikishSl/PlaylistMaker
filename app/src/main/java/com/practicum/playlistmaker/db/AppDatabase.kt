package com.practicum.playlistmaker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.PlaylistDao
import com.practicum.playlistmaker.PlaylistEntity

@Database(entities = [FavoriteTrackEntity::class, PlaylistEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteTrackDao(): FavoriteTrackDao
    abstract fun playlistDao(): PlaylistDao
}