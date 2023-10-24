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

class RecyclerSearchAdapter(private val context: Context, private val tracks: List<Track>):
    RecyclerView.Adapter<RecyclerSearchAdapter.TrackHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.search_list_item, parent, false)
        return TrackHolder(itemView)
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(trackHolder: TrackHolder, position: Int) {
        val track = tracks[position]
        trackHolder.bind(track)
    }

    class TrackHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val trackName: TextView = itemView.findViewById(R.id.search_track_name)
        val artistName: TextView = itemView.findViewById(R.id.search_artist_name)
        val trackTime: TextView = itemView.findViewById(R.id.search_track_time)
        val artworkUrl100: ImageView = itemView.findViewById(R.id.search_track_image)

        fun bind(track: Track) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = track.trackTime
            Glide.with(itemView.context)
                .load(track.artworkUrl100)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(itemView.context, 2f)))
                .placeholder(R.drawable.ic_placeholder)
                .into(artworkUrl100)
        }
    }
}