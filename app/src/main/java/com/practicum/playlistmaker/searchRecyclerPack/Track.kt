package com.practicum.playlistmaker.searchRecyclerPack

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val trackId: Int
)
