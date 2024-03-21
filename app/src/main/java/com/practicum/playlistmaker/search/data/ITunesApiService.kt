package com.practicum.playlistmaker.search.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): TrackResult
}