package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.AudioPlayerInteractor

class AudioPlayerInteractorImpl(private val mediaPlayerProvider: () -> MediaPlayer) : AudioPlayerInteractor {
    private var mediaPlayer: MediaPlayer? = null

    override fun setDataSource(url: String) {
        mediaPlayer?.release()
        mediaPlayer = mediaPlayerProvider().apply {
            setDataSource(url)
            prepareAsync()
        }
    }

    override fun prepareAsync() {
        mediaPlayer?.prepareAsync()
    }

    override fun start() {
        mediaPlayer?.start()
    }

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    override fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        mediaPlayer?.setOnCompletionListener(listener)
    }

    override fun release() {
        releaseMediaPlayer()
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}