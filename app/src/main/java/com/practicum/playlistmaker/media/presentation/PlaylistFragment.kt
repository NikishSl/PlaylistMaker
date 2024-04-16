package com.practicum.playlistmaker.media.presentation

import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R

class PlaylistFragment : Fragment() {

    private val viewModel: PlaylistViewModel by viewModel()

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
            navController.navigate(R.id.createPlaylistFragment)
        }
        return view
    }
}