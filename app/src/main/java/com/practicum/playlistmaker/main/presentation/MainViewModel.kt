package com.practicum.playlistmaker.main.presentation


import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.media.presentation.MediaActivity
import com.practicum.playlistmaker.search.presentation.SearchActivity
import com.practicum.playlistmaker.settings.presentation.SettingsActivity

class MainViewModel : ViewModel() {

    fun onSearchButtonClicked(context: Context) {
        val searchIntent = Intent(context, SearchActivity::class.java)
        context.startActivity(searchIntent)
    }

    fun onMediaButtonClicked(context: Context) {
        val mediaIntent = Intent(context, MediaActivity::class.java)
        context.startActivity(mediaIntent)
    }

    fun onSettingsButtonClicked(context: Context) {
        val settingsIntent = Intent(context, SettingsActivity::class.java)
        context.startActivity(settingsIntent)
    }
}