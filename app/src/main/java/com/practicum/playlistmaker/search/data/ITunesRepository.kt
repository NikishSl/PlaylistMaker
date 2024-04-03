package com.practicum.playlistmaker.search.data

import kotlinx.coroutines.flow.Flow

interface ITunesRepository {
    fun searchTracks(searchText: String): Flow<List<Track>>
}