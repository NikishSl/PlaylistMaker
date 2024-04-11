package com.practicum.playlistmaker.media.data

import com.practicum.playlistmaker.media.domain.FavoritesInteractor
import com.practicum.playlistmaker.search.data.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val repository: FavoritesRepository) : FavoritesInteractor {
    override suspend fun addTrackToFavorites(track: Track) {
        repository.addTrackToFavorites(track)
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        repository.removeTrackFromFavorites(track)
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> {
        return repository.getAllFavoriteTracks()
    }
}