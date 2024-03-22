package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.data.ITunesApiService
import com.practicum.playlistmaker.search.data.ITunesRepository
import com.practicum.playlistmaker.search.data.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ITunesRepositoryImpl(private val iTunesApiService: ITunesApiService) : ITunesRepository {
    override suspend fun searchTracks(searchText: String): Flow<List<Track>> {
        val result = iTunesApiService.search(searchText)
        return flow {
            emit(result.results)
        }
    }
}