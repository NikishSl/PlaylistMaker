package com.practicum.playlistmaker.player.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.data.Track
import com.practicum.playlistmaker.dpToPx
import com.practicum.playlistmaker.timeFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val TRACK_KEY = "track"
        private const val YEAR_START_INDEX = 0
        private const val YEAR_END_INDEX = 4
    }

    private val viewModel: PlayerViewModel by viewModel()
    private lateinit var playerPlayTrack: ImageButton
    private lateinit var playerTimePlnw: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backButton = findViewById<ImageButton>(R.id.player_back_button)

        backButton.setOnClickListener {
            finish()
        }

        viewModel.track.observe(this) { track ->
            if (track != null) {
                bindTrack(track)
            }
        }

        val track = intent.getParcelableExtra<Track>(TRACK_KEY)
        track?.let { viewModel.setTrack(it) }

        playerPlayTrack = findViewById(R.id.player_play_track)
        playerTimePlnw = findViewById(R.id.player_time_plnw)

        playerPlayTrack.setOnClickListener {
            viewModel.playOrPause()
            updatePlayPauseButton()
        }

        viewModel.audioPlayerInteractor.setOnCompletionListener {
            playerTimePlnw.text = timeFormat.format(0)
            viewModel.audioPlayerInteractor.seekTo(0)
            updatePlayPauseButton()
        }

        viewModel.trackTime.observe(this) { currentTime ->
            playerTimePlnw.text = timeFormat.format(currentTime.toLong())
        }
    }

    private fun bindTrack(track: Track) {
        val playerTrackName = findViewById<TextView>(R.id.player_track_name)
        val playerArtistName = findViewById<TextView>(R.id.player_artist_name)
        val playerTrackTime = findViewById<TextView>(R.id.player_track_time_mills)
        val playerCollectionName = findViewById<TextView>(R.id.player_collection_name)
        val playerReleaseDate = findViewById<TextView>(R.id.release_date)
        val playerPrimaryGenre = findViewById<TextView>(R.id.player_primary_genre)
        val playerCountry = findViewById<TextView>(R.id.player_country_name)
        val playerArtworkImage = findViewById<ImageView>(R.id.player_image)

        playerTrackName.text = track.trackName
        playerArtistName.text = track.artistName
        playerTrackTime.text = timeFormat.format(track.trackTimeMillis.toLong())

        if (!track.collectionName.isNullOrEmpty()) {
            playerCollectionName.text = track.collectionName
        } else {
            playerCollectionName.isVisible = false
        }

        playerReleaseDate.text = getYearFromDate(track.releaseDate)
        playerPrimaryGenre.text = track.primaryGenreName
        playerCountry.text = track.country

        Glide.with(this)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .centerCrop()
            .transform(RoundedCorners(dpToPx(this, 8f)))
            .placeholder(R.drawable.ic_placeholder_big)
            .into(playerArtworkImage)
    }

    private fun getYearFromDate(date: String?): String {
        return date?.substring(YEAR_START_INDEX, YEAR_END_INDEX) ?: ""
    }

    private fun updatePlayPauseButton() {
        val isPlaying = viewModel.audioPlayerInteractor.isPlaying()
        val iconResource = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play_track
        playerPlayTrack.setImageResource(iconResource)
    }

    override fun onResume() {
        super.onResume()
        viewModel.startTrackTimeUpdates()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopTrackTimeUpdates()
        viewModel.audioPlayerInteractor.pause()
        updatePlayPauseButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.audioPlayerInteractor.release()
    }
}

