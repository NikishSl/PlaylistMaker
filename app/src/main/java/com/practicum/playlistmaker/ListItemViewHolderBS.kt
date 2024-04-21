package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListItemViewHolderBS(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val albumImgBS: ImageView = itemView.findViewById(R.id.album_img_bs)
    val albumNameBS: TextView = itemView.findViewById(R.id.album_name_bs)
    val quantityTracksBS: TextView = itemView.findViewById(R.id.quantity_tracks_bs)
}