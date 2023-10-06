package com.practicum.playlistmaker.searchRecyclerPack

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.dpToPx

class RecyclerSearch(private val context: Context, private val tracks: List<Track>):
    RecyclerView.Adapter<RecyclerSearch.TrackHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.search_list_item, parent, false)
        return TrackHolder(itemView)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(trackHolder: TrackHolder, position: Int) {
        val track = tracks[position]

        trackHolder.trackName.setText(track.trackName)
        trackHolder.artistName.setText(track.artistName)
        trackHolder.trackTime.setText(track.trackTime)
        Glide.with(context)
            .load(track.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(context, 2f)))
            .placeholder(R.drawable.ic_placeholder)
            .into(trackHolder.artworkUrl100)
    }

    class TrackHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val trackName: TextView
        val artistName: TextView
        val trackTime: TextView
        val artworkUrl100: ImageView

        init {
            trackName = itemView.findViewById(R.id.search_track_name)
            artistName = itemView.findViewById(R.id.search_artist_name)
            trackTime = itemView.findViewById(R.id.search_track_time)
            artworkUrl100 = itemView.findViewById(R.id.search_track_image)
        }

    }

}