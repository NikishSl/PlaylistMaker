package com.practicum.playlistmaker.media.presentation

import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.media.createPlaylist.presentation.ListItemAdapter
import com.practicum.playlistmaker.R

class PlaylistFragment : Fragment() {

    private val viewModel: PlaylistViewModel by viewModel()
    private lateinit var playlistAdapter: ListItemAdapter

    companion object {
        fun newInstance(): PlaylistFragment {
            return PlaylistFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlist, container, false)
        val newPlaylistButton = view.findViewById<Button>(R.id.new_playlist)
        newPlaylistButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.createPlaylistFragment, Bundle().apply {
                putParcelable("playlist", null)
            })
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.playlist_recycler_view)
        playlistAdapter = ListItemAdapter(emptyList()) { playlistId ->
            playlistAdapter.navigateToPlaylistOpenFragment(playlistId, findNavController())
        }
        recyclerView.adapter = playlistAdapter

        val layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager

        observeViewModel()

        viewModel.loadPlaylists()

        return view
    }

    private fun observeViewModel() {
        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            if (playlists.isNotEmpty()) {
                playlistAdapter.updatePlaylists(playlists)
                showRecyclerView()
            } else {
                showNoPlaylistsMessage()
            }
        }
    }

    private fun showRecyclerView() {
        view?.findViewById<RecyclerView>(R.id.playlist_recycler_view)?.visibility = View.VISIBLE
        view?.findViewById<ImageView>(R.id.no_playlist_image)?.visibility = View.GONE
        view?.findViewById<TextView>(R.id.no_playlist_text)?.visibility = View.GONE
    }

    private fun showNoPlaylistsMessage() {
        view?.findViewById<RecyclerView>(R.id.playlist_recycler_view)?.visibility = View.GONE
        view?.findViewById<ImageView>(R.id.no_playlist_image)?.visibility = View.VISIBLE
        view?.findViewById<TextView>(R.id.no_playlist_text)?.visibility = View.VISIBLE
    }

}