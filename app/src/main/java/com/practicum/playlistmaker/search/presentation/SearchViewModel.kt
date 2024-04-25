package com.practicum.playlistmaker.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.data.ITunesRepository
import com.practicum.playlistmaker.search.data.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchViewModel(private val iTunesRepository: ITunesRepository) : ViewModel() {

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks: Flow<List<Track>> = _tracks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: Flow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<Throwable?>(null)
    val error: Flow<Throwable?> = _error.asStateFlow()

    fun searchTracks(searchText: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                iTunesRepository.searchTracks(searchText)
                    .map { tracks -> iTunesRepository.checkFavorites(tracks) }
                    .collect { tracks ->
                        _tracks.value = tracks
                    }
            } catch (e: Exception) {
                _error.value = e
                _tracks.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getSearchHistoryTracks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                iTunesRepository.getSearchHistoryTracks()
                    .map { tracks -> iTunesRepository.checkFavorites(tracks) }
                    .collect { tracks ->
                        _tracks.value = tracks
                    }
            } catch (e: Exception) {
                _tracks.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}