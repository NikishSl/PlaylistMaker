package com.practicum.playlistmaker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.player.presentation.PlayerViewModel
import com.practicum.playlistmaker.search.data.Track

import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    companion object {
        private const val TRACK_KEY = "track"
        private const val YEAR_START_INDEX = 0
        private const val YEAR_END_INDEX = 4
    }

    private val viewModel: PlayerViewModel by viewModel()
    private lateinit var playerPlayTrack: ImageButton
    private lateinit var playerTimePlnw: TextView
    private lateinit var likeButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player, container, false)

        val backButton = view.findViewById<ImageButton>(R.id.player_back_button)
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.track.observe(viewLifecycleOwner) { track ->
            if (track != null) {
                bindTrack(track)
            }
        }

        val track = arguments?.getParcelable<Track>(TRACK_KEY)
        track?.let { viewModel.setTrack(it) }

        playerPlayTrack = view.findViewById(R.id.player_play_track)
        playerTimePlnw = view.findViewById(R.id.player_time_plnw)
        likeButton = view.findViewById(R.id.player_like_track)

        playerPlayTrack.setOnClickListener {
            viewModel.playOrPause()
            updatePlayPauseButton()
        }

        likeButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        viewModel.audioPlayerInteractor.setOnCompletionListener {
            playerTimePlnw.text = timeFormat.format(0)
            viewModel.audioPlayerInteractor.seekTo(0)
            updatePlayPauseButton()
        }

        viewModel.trackTime.observe(viewLifecycleOwner) { currentTime ->
            playerTimePlnw.text = timeFormat.format(currentTime.toLong())
        }

        viewModel.isFavorite.observe(viewLifecycleOwner, Observer { isFavorite ->
            val iconResource = if (isFavorite) R.drawable.ic_like else R.drawable.ic_like_track
            likeButton.setImageResource(iconResource)
        })

        return view
    }

    private fun bindTrack(track: Track) {
        val playerTrackName = requireView().findViewById<TextView>(R.id.player_track_name)
        val playerArtistName = requireView().findViewById<TextView>(R.id.player_artist_name)
        val playerTrackTime = requireView().findViewById<TextView>(R.id.player_track_time_mills)
        val playerCollectionName = requireView().findViewById<TextView>(R.id.player_collection_name)
        val playerReleaseDate = requireView().findViewById<TextView>(R.id.release_date)
        val playerPrimaryGenre = requireView().findViewById<TextView>(R.id.player_primary_genre)
        val playerCountry = requireView().findViewById<TextView>(R.id.player_country_name)
        val playerArtworkImage = requireView().findViewById<ImageView>(R.id.player_image)

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
            .transform(RoundedCorners(dpToPx(requireContext(), 8f)))
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