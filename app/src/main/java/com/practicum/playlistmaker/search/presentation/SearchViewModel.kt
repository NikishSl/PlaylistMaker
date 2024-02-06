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

class SearchViewModel(private val iTunesApiService: ITunesApiService) : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracks

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchTracks(searchText: String) {
        _isLoading.value = true

        iTunesApiService.search(searchText).enqueue(object : Callback<TrackResult> {
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