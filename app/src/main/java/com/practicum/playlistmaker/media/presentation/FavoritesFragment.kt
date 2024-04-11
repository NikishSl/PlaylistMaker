package com.practicum.playlistmaker.media.presentation

import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.presentation.RecyclerSearchAdapter

class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModel()
    private lateinit var recyclerView: RecyclerView
    private lateinit var placeholder: FrameLayout
    private lateinit var adapter: RecyclerSearchAdapter

    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.favorites_recycler)
        placeholder = view.findViewById(R.id.favorites_placeholder)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = RecyclerSearchAdapter(requireContext(), mutableListOf(), null, lifecycleScope)
        recyclerView.adapter = adapter

        viewModel.favoriteTracks.observe(viewLifecycleOwner) { tracks ->
            if (tracks.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                placeholder.visibility = View.GONE
            } else {
                recyclerView.visibility = View.GONE
                placeholder.visibility = View.VISIBLE
            }
            adapter.submitTracks(tracks)
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteTracks()
    }
}