package com.practicum.playlistmaker.player.presentation

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.changeTextTrack
import com.practicum.playlistmaker.db.PlaylistEntity
import com.practicum.playlistmaker.dpToPxView
import java.io.File

class ListItemAdapterBS(private var playlistsBS: List<PlaylistEntity>, private val itemClickListener: (PlaylistEntity) -> Unit):RecyclerView.Adapter<ListItemViewHolderBS>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolderBS {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bottom_sheet_recycler, parent, false)
        return ListItemViewHolderBS(view)
    }

    override fun getItemCount(): Int {
        return playlistsBS.size
    }

    override fun onBindViewHolder(holder: ListItemViewHolderBS, position: Int) {
        val playlistBS = playlistsBS[position]
        holder.albumNameBS.text = playlistBS.name
        holder.quantityTracksBS.text = playlistBS.trackCount.toString()
        holder.trackBS.text = changeTextTrack(playlistBS.trackCount)
        val fileBS = File(playlistBS.coverImageFilePath)
        val uriBS = Uri.fromFile(fileBS)

        Glide.with(holder.itemView)
            .load(uriBS)
            .transform(CenterCrop(), RoundedCorners(dpToPxView(holder.itemView, 2f)))
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.albumImgBS)

        holder.itemView.setOnClickListener {
            itemClickListener.invoke(playlistBS)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePlaylists(newPlaylists: List<PlaylistEntity>) {
        playlistsBS = newPlaylists
        notifyDataSetChanged()
    }
}