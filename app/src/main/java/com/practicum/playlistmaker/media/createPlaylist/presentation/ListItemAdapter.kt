package com.practicum.playlistmaker.media.createPlaylist.presentation

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.changeTextTrackWithNumb
import com.practicum.playlistmaker.db.PlaylistEntity
import com.practicum.playlistmaker.dpToPxView
import java.io.File

class ListItemAdapter(private var playlists: List<PlaylistEntity>,
                      private val onItemClick: (Long) -> Unit) :
    RecyclerView.Adapter<ListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_layout, parent, false)
        return ListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.coverName.text = playlist.name
        holder.quantityTracks.text = changeTextTrackWithNumb(playlist.trackCount)
        val file = File(playlist.coverImageFilePath)
        val uri = Uri.fromFile(file)

        Glide.with(holder.itemView)
            .load(uri)
            .transform(CenterCrop(),RoundedCorners(dpToPxView(holder.itemView, 8f)))
            .placeholder(R.drawable.ic_placeholder_med)
            .into(holder.coverImage)

        holder.itemView.setOnClickListener {
            onItemClick(playlist.playlistId)
        }
    }

    fun navigateToPlaylistOpenFragment(playlistId: Long, navController: NavController) {
        val bundle = Bundle().apply {
            putLong("playlistId", playlistId)
        }
        navController.navigate(R.id.playlistOpenFragment, bundle)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePlaylists(newPlaylists: List<PlaylistEntity>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }
}