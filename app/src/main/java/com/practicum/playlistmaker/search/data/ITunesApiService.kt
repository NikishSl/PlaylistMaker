package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.TrackResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResult>
}