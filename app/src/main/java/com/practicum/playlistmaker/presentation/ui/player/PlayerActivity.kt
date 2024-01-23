package com.practicum.playlistmaker.presentation.ui.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.dpToPx
import com.practicum.playlistmaker.timeFormat

class PlayerActivity : AppCompatActivity() {

    private lateinit var audioPlayerInteractor: AudioPlayerInteractor
    private lateinit var playerPlayTrack: ImageButton
    private lateinit var playerTimePlnw: TextView
    private lateinit var handler: Handler
    private lateinit var updateSeekBar: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backButton = findViewById<ImageButton>(R.id.player_back_button)

        backButton.setOnClickListener {
            finish()
        }

        val track = intent.getParcelableExtra<Track>("track")

        if (track != null) {
            val trackName = track.trackName
            val artistName = track.artistName
            val trackTimeMillis = track.trackTimeMillis
            val collectionName = track.collectionName
            val releaseDate = track.releaseDate
            val primaryGenreName = track.primaryGenreName
            val country = track.country
            val artworkUrl100 = track.artworkUrl100
            val previewUrl = track.previewUrl

            val playerAlbum = findViewById<TextView>(R.id.player_album)
            val playerTrackName = findViewById<TextView>(R.id.player_track_name)
            val playerArtistName = findViewById<TextView>(R.id.player_artist_name)
            val playerTrackTime = findViewById<TextView>(R.id.player_track_time_mills)
            val playerCollectionName = findViewById<TextView>(R.id.player_collection_name)
            val playerReleaseDate = findViewById<TextView>(R.id.release_date)
            val playerPrimaryGenre = findViewById<TextView>(R.id.player_primary_genre)
            val playerCountry = findViewById<TextView>(R.id.player_country_name)
            val playerArtworkImage = findViewById<ImageView>(R.id.player_image)

            audioPlayerInteractor = AudioPlayerInteractorImpl()
            audioPlayerInteractor.setDataSource(previewUrl)

            playerPlayTrack = findViewById<ImageButton>(R.id.player_play_track)
            playerTimePlnw = findViewById<TextView>(R.id.player_time_plnw)
            handler = Handler(Looper.getMainLooper())

            playerPlayTrack.setOnClickListener {
                if (audioPlayerInteractor.isPlaying()) {
                    audioPlayerInteractor.pause()
                } else {
                    audioPlayerInteractor.start()
                    updateSeekBar()
                }
                updatePlayPauseButton()
            }

            audioPlayerInteractor.setOnCompletionListener {
                playerTimePlnw.text = timeFormat.format(0)
                audioPlayerInteractor.seekTo(0)
                updatePlayPauseButton()
            }

            playerTrackName.text = trackName
            playerArtistName.text = artistName
            playerTrackTime.text = timeFormat.format(trackTimeMillis.toLong())

            if (collectionName != null) {
                playerCollectionName.text = collectionName
            } else {
                playerAlbum.isVisible = false
                playerCollectionName.isVisible = false
            }

            playerReleaseDate.text = releaseDate?.substring(0, 4)
            playerPrimaryGenre.text = primaryGenreName
            playerCountry.text = country

            Glide.with(this)
                .load(artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
                .centerCrop()
                .transform(RoundedCorners(dpToPx(this, 8f)))
                .placeholder(R.drawable.ic_placeholder_big)
                .into(playerArtworkImage)
        }
    }

    private fun updatePlayPauseButton() {
        val isPlaying = audioPlayerInteractor.isPlaying()
        val iconResource = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play_track
        playerPlayTrack.setImageResource(iconResource)
    }

    private fun updateSeekBar() {
        updateSeekBar = Runnable {
            val currentTime = audioPlayerInteractor.getCurrentPosition()
            playerTimePlnw.text = timeFormat.format(currentTime.toLong())
            handler.postDelayed(updateSeekBar, 1000)
        }
        handler.postDelayed(updateSeekBar, 0)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateSeekBar)
        audioPlayerInteractor.pause()
        updatePlayPauseButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerInteractor.release()
    }
}

