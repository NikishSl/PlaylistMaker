package com.practicum.playlistmaker.searchRecyclerPack

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.PlayerActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.SearchHistoryManager
import com.practicum.playlistmaker.dpToPx
import java.text.SimpleDateFormat
import java.util.Locale

class RecyclerSearchAdapter(
    private val context: Context,
    private var tracks: MutableList<Track>,
    private val searchHistoryManager: SearchHistoryManager?
) : RecyclerView.Adapter<RecyclerSearchAdapter.TrackHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.search_list_item, parent, false)
        return TrackHolder(itemView)
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            searchHistoryManager?.addToSearchHistory(track)
            navigateToAudioPlayer(track)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTracks(newTracks: MutableList<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }


    private fun navigateToAudioPlayer(track: Track) {
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("trackName", track.trackName)
        intent.putExtra("artistName", track.artistName)
        intent.putExtra("trackTimeMillis", track.trackTimeMillis)
        intent.putExtra("artworkUrl100", track.artworkUrl100)
        intent.putExtra("collectionName", track.collectionName)
        intent.putExtra("releaseDate", track.releaseDate)
        intent.putExtra("primaryGenreName", track.primaryGenreName)
        intent.putExtra("country", track.country)
        context.startActivity(intent)
    }

    class TrackHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackName: TextView = itemView.findViewById(R.id.search_track_name)
        private val artistName: TextView = itemView.findViewById(R.id.search_artist_name)
        private val trackTime: TextView = itemView.findViewById(R.id.search_track_time)
        private val artworkUrl100: ImageView = itemView.findViewById(R.id.search_track_image)

        fun bind(track: Track) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())
            Glide.with(itemView.context)
                .load(track.artworkUrl100)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(itemView.context, 2f)))
                .placeholder(R.drawable.ic_placeholder)
                .into(artworkUrl100)
        }
    }
}