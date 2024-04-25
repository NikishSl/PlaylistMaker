package com.practicum.playlistmaker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistOpenViewModel : ViewModel() {
    private val _backButtonClickedPL = MutableLiveData<Unit>()
    val backButtonClickedPL: LiveData<Unit>
        get() = _backButtonClickedPL

    fun onBackButtonClickedPL() {
        _backButtonClickedPL.value = Unit
    }
}