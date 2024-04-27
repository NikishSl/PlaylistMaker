package com.practicum.playlistmaker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistOpenFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistOpenFragment()
    }

    private val viewModel: PlaylistOpenViewModel by viewModel()
    private lateinit var backButton: ImageButton
    private lateinit var playlystOpenName: TextView
    private lateinit var playlistOpenDescription: TextView
    private lateinit var playlistOpenSumTime: TextView
    private lateinit var playlistOpenQuantityTracks: TextView
    private lateinit var playlistOpenImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlist_open, container, false)
        backButton = view.findViewById(R.id.playlist_open_back_button)
        playlystOpenName = view.findViewById(R.id.playlyst_open_name)
        playlistOpenDescription = view.findViewById(R.id.playlist_open_description)
        playlistOpenSumTime = view.findViewById(R.id.playlist_open_sum_time)
        playlistOpenQuantityTracks = view.findViewById(R.id.playlist_open_quantity_tracks)
        playlistOpenImage = view.findViewById(R.id.playlist_open_image)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton.setOnClickListener {
                requireActivity().onBackPressed()
        }

        viewModel.backButtonClickedPL.observe(viewLifecycleOwner, Observer {
            requireActivity().onBackPressed()
        })

        viewModel.playlist.observe(viewLifecycleOwner, Observer { playlist ->
            Log.d("PlaylistFragment", "Playlist description: ${playlist?.description}")
            playlystOpenName.text = playlist?.name
            playlistOpenDescription.text = playlist?.description
            if (playlist != null) {
                playlistOpenQuantityTracks.text = changeTextTrackWithNumb(playlist.trackCount)
            }
            val file = File(playlist?.coverImageFilePath)

            Glide.with(requireContext())
                .load(file)
                .transform(CenterCrop())
                .placeholder(R.drawable.ic_placeholder_med)
                .into(playlistOpenImage)
        })

        viewModel.formattedDuration.observe(viewLifecycleOwner, Observer { formattedTime ->
            playlistOpenSumTime.text = formattedTime
        })

        val playlistId = arguments?.getLong("playlistId") ?: -1
        viewModel.loadPlaylist(playlistId)
    }

}