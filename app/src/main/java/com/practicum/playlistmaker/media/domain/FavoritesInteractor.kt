package com.practicum.playlistmaker.media.domain

import com.practicum.playlistmaker.search.data.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun addTrackToFavorites(track: Track)
    suspend fun removeTrackFromFavorites(track: Track)
    fun getAllFavoriteTracks(): Flow<List<Track>>
}