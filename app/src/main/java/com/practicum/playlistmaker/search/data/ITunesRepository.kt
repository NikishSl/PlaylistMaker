package com.practicum.playlistmaker.search.data

import kotlinx.coroutines.flow.Flow

interface ITunesRepository {
    fun searchTracks(searchText: String): Flow<List<Track>>
    suspend fun checkFavorites(tracks: List<Track>): List<Track>
    suspend fun getSearchHistoryTracks(): Flow<List<Track>>
}