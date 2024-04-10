package com.practicum.playlistmaker

import com.practicum.playlistmaker.db.AppDatabase
import com.practicum.playlistmaker.db.FavoriteTrackEntity
import com.practicum.playlistmaker.search.data.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackConverter: TrackConverter
) : FavoritesRepository {
    override suspend fun addTrackToFavorites(track: Track) {
        val favoriteTrackEntity = trackConverter.map(track)
        appDatabase.favoriteTrackDao().insertTrack(favoriteTrackEntity)
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        val favoriteTrackEntity = trackConverter.map(track)
        appDatabase.favoriteTrackDao().deleteTrack(favoriteTrackEntity)
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> = flow {
        val favoriteTrackEntityList = appDatabase.favoriteTrackDao().getAllFavoriteTracks()
        val trackList = convertFromTrackEntity(favoriteTrackEntityList.sortedByDescending { it.addTimeStamp } )
        val favoriteTrackEntityListIds = appDatabase.favoriteTrackDao().getAllFavoriteTrackIds()
        for (track in trackList) track.isFavorite = favoriteTrackEntityListIds.contains(track.trackId)
        emit(trackList)
    }

    private fun convertFromTrackEntity(favoriteTrackEntityList: List<FavoriteTrackEntity>): List<Track> {
        return favoriteTrackEntityList.map { favoriteTrackEntity -> trackConverter.map(favoriteTrackEntity) }
    }

}