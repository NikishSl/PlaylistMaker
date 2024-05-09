package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaylistOpenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val trackNameOpen: TextView = itemView.findViewById(R.id.search_track_name)
    val artistNameOpen: TextView = itemView.findViewById(R.id.search_artist_name)
    val trackTimeOpen: TextView = itemView.findViewById(R.id.search_track_time)
    val albumImgOpen: ImageView = itemView.findViewById(R.id.search_track_image)
}