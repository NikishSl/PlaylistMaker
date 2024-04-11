package com.practicum.playlistmaker.media.presentation


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.FavoritesInteractor
import com.practicum.playlistmaker.search.data.Track
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {

    private val _favoriteTracks = MutableLiveData<List<Track>>()
    val favoriteTracks: LiveData<List<Track>> = _favoriteTracks

    init {
        getFavoriteTracks()
    }

    fun getFavoriteTracks() {
        viewModelScope.launch {
            favoritesInteractor.getAllFavoriteTracks().collect { tracks ->
                _favoriteTracks.value = tracks
            }
        }
    }
}