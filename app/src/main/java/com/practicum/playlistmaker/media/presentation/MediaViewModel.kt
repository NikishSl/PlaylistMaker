package com.practicum.playlistmaker.media.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MediaViewModel : ViewModel() {

    companion object {
        private const val FAVORITE_TRACKS = "Избранные треки"
        private const val PLAYLISTS = "Плейлисты"
    }

    private val _tabTitles = MutableLiveData<List<String>>()
    val tabTitles: LiveData<List<String>>
        get() = _tabTitles

    init {
        _tabTitles.value = listOf(FAVORITE_TRACKS, PLAYLISTS)
    }
}