package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaButton = findViewById<Button>(R.id.media_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)

        searchButton.setOnClickListener {
            val searchButton = Intent(this, SearchActivity::class.java)
            startActivity(searchButton)
        }


        mediaButton.setOnClickListener {
            val mediaButton = Intent(this, MediaActivity::class.java)
            startActivity(mediaButton)
        }

        settingsButton.setOnClickListener {
            val settingsButton = Intent(this, SettingsActivity::class.java)
            startActivity(settingsButton)
        }


    }
}