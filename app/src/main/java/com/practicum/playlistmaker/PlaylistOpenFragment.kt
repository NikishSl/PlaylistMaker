package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
    private lateinit var playlistRecyclerOpenBS: RecyclerView
    private lateinit var playlistOpenAdapter: PlaylistOpenAdapter

    @SuppressLint("MissingInflatedId")
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
        playlistRecyclerOpenBS = view.findViewById(R.id.playlist_open_recycler_bottom_sheet)

        playlistOpenAdapter = PlaylistOpenAdapter(emptyList(),
            onTrackClick = { track ->
                playlistOpenAdapter.navigateToPlaylistOpenFragment(track, findNavController())
            },
            onTrackLongClickListener = { track ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Подтверждение удаления")
                .setMessage("Вы уверены, что хотите удалить трек ${track.trackName}?")
                .setNegativeButton("Отмена", null)
                .setPositiveButton("Удалить") { dialog, which -> val playlistId = arguments?.getLong("playlistId") ?: -1
                    viewModel.deleteTrackFromPlaylist(playlistId, track.trackId)
                }
                .show()
        })

        playlistRecyclerOpenBS.adapter = playlistOpenAdapter
        val layoutOpenManager = LinearLayoutManager(requireContext())
        playlistRecyclerOpenBS.layoutManager = layoutOpenManager
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.tracksOpen.observe(viewLifecycleOwner, Observer { tracksOpen ->
            playlistOpenAdapter.updatePlaylists(tracksOpen)
        })


        backButton.setOnClickListener {
                requireActivity().onBackPressed()
        }

        viewModel.backButtonClickedPL.observe(viewLifecycleOwner, Observer {
            requireActivity().onBackPressed()
        })

        viewModel.playlist.observe(viewLifecycleOwner, Observer { playlist ->
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