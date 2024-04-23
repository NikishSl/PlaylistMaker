package com.practicum.playlistmaker.player.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.db.PlaylistTrackEntity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.dpToPx
import com.practicum.playlistmaker.search.data.Track
import com.practicum.playlistmaker.timeFormat

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
    private lateinit var playerAddTrack: ImageButton
    private lateinit var newPlaylistBottomSheet: Button
    private lateinit var playlistAdapter: ListItemAdapterBS
    private lateinit var bottomSheetContainer: LinearLayout
    private lateinit var overlay: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player, container, false)

        val backButton = view.findViewById<ImageButton>(R.id.player_back_button)
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        bottomSheetContainer = view.findViewById(R.id.playlists_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }


        val recyclerViewBS = view.findViewById<RecyclerView>(R.id.playlist_recycler_view_bottom_sheet)
        playlistAdapter = ListItemAdapterBS(emptyList()) { playlist ->
            val track = viewModel.track.value
            track?.let {
                val playlistTrack = PlaylistTrackEntity(
                    trackId = it.trackId,
                    artworkUrl100 = it.artworkUrl100 ?: "",
                    trackName = it.trackName ?: "",
                    artistName = it.artistName ?: "",
                    collectionName = it.collectionName ?: "",
                    releaseDate = it.releaseDate ?: "",
                    primaryGenreName = it.primaryGenreName ?: "",
                    country = it.country ?: "",
                    trackTimeMillis = it.trackTimeMillis ?: 0,
                    previewUrl = it.previewUrl ?: ""
                )
                viewModel.checkTrackInPlaylist(playlist.playlistId, playlistTrack.trackId) { isInPlaylist ->
                    val playlistName = playlist.name
                    if (!isInPlaylist) {
                        viewModel.insertTrackIntoPlaylist(playlistTrack, playlist.playlistId)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        val message = "Добавлено в плейлист $playlistName"
                        showToast(message)
                    } else {
                        val message = "Трек уже добавлен в плейлист $playlistName"
                        showToast(message)
                    }
                }
            }
        }
        recyclerViewBS.adapter = playlistAdapter

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerViewBS.layoutManager = layoutManager

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
        overlay = view.findViewById(R.id.overlay)
        playerAddTrack = view.findViewById(R.id.player_add_track)
        newPlaylistBottomSheet = view.findViewById(R.id.new_playlist_bottom_sheet)



        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }
                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        playerPlayTrack.setOnClickListener {
            viewModel.playOrPause()
            updatePlayPauseButton()
        }

        playerAddTrack.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        newPlaylistBottomSheet.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.createPlaylistFragment)
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

        observeViewModel()

        viewModel.getAllPlaylists()

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

    private fun observeViewModel() {
        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            playlistAdapter.updatePlaylists(playlists)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun updatePlayPauseButton() {
        val isPlaying = viewModel.audioPlayerInteractor.isPlaying()
        val iconResource = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play_track
        playerPlayTrack.setImageResource(iconResource)
    }

    override fun onResume() {
        super.onResume()
        viewModel.startTrackTimeUpdates()
        viewModel.getAllPlaylists()
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