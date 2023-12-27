package com.practicum.playlistmaker.presentation

import com.practicum.playlistmaker.domain.models.Track

data class TrackResult(
    val resultCount: Int,
    val results: List<Track>
)
