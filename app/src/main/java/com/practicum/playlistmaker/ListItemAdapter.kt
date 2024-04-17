package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.io.File

class ListItemAdapter(private var playlists: List<PlaylistEntity>) :
    RecyclerView.Adapter<ListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_layout, parent, false)
        return ListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.coverName.text = playlist.name
        holder.quantityTracks.text = playlist.trackCount.toString()
        val file = File(playlist.coverImageFilePath)
        val uri = Uri.fromFile(file)

        Glide.with(holder.itemView)
            .load(uri)
            .transform(CenterCrop(),RoundedCorners(dpToPxView(holder.itemView, 8f)))
            .placeholder(R.drawable.ic_placeholder_med)
            .into(holder.coverImage)
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