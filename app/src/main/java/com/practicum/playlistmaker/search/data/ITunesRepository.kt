package com.practicum.playlistmaker.search.data

import kotlinx.coroutines.flow.Flow

interface ITunesRepository {
    suspend fun searchTracks(searchText: String): Flow<List<Track>>
}