package com.practicum.playlistmaker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = 0,
    val name: String,
    val description: String,
    var coverImageFilePath: String,
    val trackIds: String = "",
    val trackCount: Int = 0
)
