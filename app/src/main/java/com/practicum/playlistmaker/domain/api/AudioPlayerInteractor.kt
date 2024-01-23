package com.practicum.playlistmaker.domain.api

import android.media.MediaPlayer

interface AudioPlayerInteractor {
    fun setDataSource(url: String)
    fun prepareAsync()
    fun start()
    fun pause()
    fun seekTo(position: Int)
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean
    fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener)
    fun release()
}