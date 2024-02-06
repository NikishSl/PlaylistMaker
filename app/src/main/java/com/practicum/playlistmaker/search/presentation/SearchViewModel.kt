package com.practicum.playlistmaker.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.data.ITunesApiService
import com.practicum.playlistmaker.search.data.Track
import com.practicum.playlistmaker.search.data.TrackResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewModel : ViewModel() {
    companion object {
        private const val BASE_URL = "https://itunes.apple.com"
    }

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracks

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchTracks(searchText: String) {
        _isLoading.value = true

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val iTunesApiService = retrofit.create(ITunesApiService::class.java)
        val call = iTunesApiService.search(searchText)

        call.enqueue(object : Callback<TrackResult> {
            override fun onResponse(call: Call<TrackResult>, response: Response<TrackResult>) {
                _isLoading.value = false
                val trackResult = response.body()
                if (response.isSuccessful && trackResult != null) {
                    _tracks.value = trackResult.results
                } else {
                    _tracks.value = emptyList()
                }
            }

            override fun onFailure(call: Call<TrackResult>, t: Throwable) {
                _isLoading.value = false
                _tracks.value = emptyList()
            }
        })
    }
}