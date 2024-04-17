package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val coverImage: ImageView = itemView.findViewById(R.id.cover_image)
    val coverName: TextView = itemView.findViewById(R.id.cover_name)
    val quantityTracks: TextView = itemView.findViewById(R.id.quantity_tracks)
}