package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.data.ITunesApiService
import com.practicum.playlistmaker.search.data.ITunesRepository
import com.practicum.playlistmaker.search.data.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ITunesRepositoryImpl(private val iTunesApiService: ITunesApiService) : ITunesRepository {
    override fun searchTracks(searchText: String): Flow<List<Track>> {
        return flow {
            val result = iTunesApiService.search(searchText)
            emit(result.results)
        }
    }
}