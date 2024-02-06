package com.practicum.playlistmaker.main.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.practicum.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaButton = findViewById<Button>(R.id.media_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)

        searchButton.setOnClickListener {
            viewModel.onSearchButtonClicked(this)
        }

        mediaButton.setOnClickListener {
            viewModel.onMediaButtonClicked(this)
        }

        settingsButton.setOnClickListener {
            viewModel.onSettingsButtonClicked(this)
        }
    }
}