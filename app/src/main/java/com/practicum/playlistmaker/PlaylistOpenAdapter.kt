package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.db.PlaylistTrackEntity



class PlaylistOpenAdapter(private var tracksOpen: List<PlaylistTrackEntity>,
                          private val onTrackClick: (Int) -> Unit,
                          private val onTrackLongClickListener: (PlaylistTrackEntity) -> Unit): RecyclerView.Adapter<PlaylistOpenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistOpenViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_list_item, parent, false)
        return PlaylistOpenViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracksOpen.size
    }

    override fun onBindViewHolder(holder: PlaylistOpenViewHolder, position: Int) {
        val tracksOpen = tracksOpen[position]
        holder.artistNameOpen.text = tracksOpen.artistName
        holder.trackNameOpen.text = tracksOpen.trackName
        holder.trackTimeOpen.text = timeFormat.format(tracksOpen.trackTimeMillis)

        Glide.with(holder.itemView)
            .load(tracksOpen.artworkUrl100)
            .transform(RoundedCorners(dpToPxView(holder.itemView, 2f)))
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.albumImgOpen)

        holder.itemView.setOnLongClickListener {
            onTrackLongClickListener(tracksOpen)
            true
        }

        holder.itemView.setOnClickListener {
            onTrackClick(tracksOpen.trackId)
        }
    }

    fun navigateToPlaylistOpenFragment(trackId: Int, navController: NavController) {
        val bundle = Bundle().apply {
            putInt("trackId", trackId)
        }
        navController.navigate(R.id.playerFragment, bundle)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updatePlaylists(newPlaylists: List<PlaylistTrackEntity>) {
        tracksOpen = newPlaylists
        notifyDataSetChanged()
    }
}