package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.db.AppDatabase
import com.practicum.playlistmaker.search.data.ITunesApiService
import com.practicum.playlistmaker.search.data.ITunesRepository
import com.practicum.playlistmaker.search.data.SearchHistoryManager
import com.practicum.playlistmaker.search.data.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ITunesRepositoryImpl(
    private val iTunesApiService: ITunesApiService,
    private val appDatabase: AppDatabase,
    private val searchHistoryManager: SearchHistoryManager
) : ITunesRepository {
    override fun searchTracks(searchText: String): Flow<List<Track>> {
        return flow {
            val result = iTunesApiService.search(searchText)
            emit(result.results)
        }
    }
    override suspend fun checkFavorites(tracks: List<Track>): List<Track> {
        val favoriteTrackIds = appDatabase.favoriteTrackDao().getAllFavoriteTrackIds()
        return tracks.map { track ->
            track.copy(isFavorite = favoriteTrackIds.contains(track.trackId))
        }
    }
    override suspend fun getSearchHistoryTracks(): Flow<List<Track>> {
        return flow {
            val searchHistory = searchHistoryManager.getSearchHistory()
            emit(searchHistory)
        }
    }
}