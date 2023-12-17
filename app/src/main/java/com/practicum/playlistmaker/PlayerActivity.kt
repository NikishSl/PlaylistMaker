package com.practicum.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
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

        val trackName = intent.getStringExtra("trackName")
        val artistName = intent.getStringExtra("artistName")
        val trackTimeMillis = intent.getIntExtra("trackTimeMillis", 0)
        val collectionName = intent.getStringExtra("collectionName")
        val releaseDate = intent.getStringExtra("releaseDate")
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")
        val artworkUrl100 = intent.getStringExtra("artworkUrl100")
        val previewUrl = intent.getStringExtra("previewUrl")

        val playerAlbum = findViewById<TextView>(R.id.player_album)
        val playerTrackName = findViewById<TextView>(R.id.player_track_name)
        val playerArtistName = findViewById<TextView>(R.id.player_artist_name)
        val playerTrackTime = findViewById<TextView>(R.id.player_track_time_mills)
        val playerCollectionName = findViewById<TextView>(R.id.player_collection_name)
        val playerReleaseDate = findViewById<TextView>(R.id.release_date)
        val playerPrimaryGenre = findViewById<TextView>(R.id.player_primary_genre)
        val playerCountry = findViewById<TextView>(R.id.player_country_name)
        val playerArtworkImage = findViewById<ImageView>(R.id.player_image)

        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()

        playerPlayTrack = findViewById<ImageButton>(R.id.player_play_track)
        playerTimePlnw = findViewById<TextView>(R.id.player_time_plnw)
        handler = Handler()

        playerPlayTrack.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            } else {
                mediaPlayer.start()
                updateSeekBar()
            }
            updatePlayPauseButton()
        }

        mediaPlayer.setOnCompletionListener {
            playerTimePlnw.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
            mediaPlayer.seekTo(0)
            updatePlayPauseButton()
        }

        playerTrackName.text = trackName
        playerArtistName.text = artistName
        playerTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis.toLong())

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

    private fun updatePlayPauseButton() {
        val isPlaying = mediaPlayer.isPlaying
        val iconResource = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play_track
        playerPlayTrack.setImageResource(iconResource)
    }

    private fun updateSeekBar() {
        updateSeekBar = Runnable {
            val currentTime = mediaPlayer.currentPosition
            playerTimePlnw.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTime.toLong())
            handler.postDelayed(updateSeekBar, 1000)
        }
        handler.postDelayed(updateSeekBar, 0)
    }

    override fun onResume() {
        super.onResume()
        if (mediaPlayer.isPlaying) {
            updateSeekBar()
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateSeekBar)
        mediaPlayer.pause()
        updatePlayPauseButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(updateSeekBar)
    }
}

