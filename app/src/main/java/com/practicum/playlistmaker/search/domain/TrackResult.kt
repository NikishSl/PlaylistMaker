package com.practicum.playlistmaker.search.domain

data class TrackResult(
    val resultCount: Int,
    val results: List<Track>
)
