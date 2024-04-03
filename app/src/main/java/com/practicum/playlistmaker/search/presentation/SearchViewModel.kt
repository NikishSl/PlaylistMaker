package com.practicum.playlistmaker.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.data.ITunesRepository
import com.practicum.playlistmaker.search.data.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val iTunesRepository: ITunesRepository) : ViewModel() {

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks: Flow<List<Track>> = _tracks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: Flow<Boolean> = _isLoading.asStateFlow()

    fun searchTracks(searchText: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                iTunesRepository.searchTracks(searchText).collect { tracks ->
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