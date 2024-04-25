package com.practicum.playlistmaker

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistOpenFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistOpenFragment()
    }

    private val viewModel: PlaylistOpenViewModel by viewModel()
    private lateinit var backButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlist_open, container, false)
        backButton = view.findViewById(R.id.playlist_open_back_button)
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
    }

}