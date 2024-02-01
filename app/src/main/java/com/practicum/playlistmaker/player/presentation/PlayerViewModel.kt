package com.practicum.playlistmaker.player.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.AudioPlayerInteractor
import com.practicum.playlistmaker.search.data.Track

class PlayerViewModel(val audioPlayerInteractor: AudioPlayerInteractor) : ViewModel() {

    companion object {
        private const val TRACK_TIME_UPDATE_DELAY_MILLIS = 1000L
    }

    private val _track = MutableLiveData<Track>()
    val track: LiveData<Track>
        get() = _track
    private val _trackTime = MutableLiveData<Int>()
    val trackTime: LiveData<Int>
        get() = _trackTime

    private var trackTimeUpdater: Runnable? = null
    private val handler = Handler(Looper.getMainLooper())

    fun setTrack(track: Track) {
        _track.value = track
        audioPlayerInteractor.setDataSource(track.previewUrl)
    }

    fun playOrPause() {
        if (audioPlayerInteractor.isPlaying()) {
            audioPlayerInteractor.pause()
        } else {
            audioPlayerInteractor.start()
        }
    }

    fun startTrackTimeUpdates() {
        trackTimeUpdater = Runnable {
            val currentTime = audioPlayerInteractor.getCurrentPosition()
            _trackTime.postValue(currentTime)
            handler.postDelayed(trackTimeUpdater!!, TRACK_TIME_UPDATE_DELAY_MILLIS)
        }
        handler.post(trackTimeUpdater!!)
    }

    fun stopTrackTimeUpdates() {
        trackTimeUpdater?.let {
            handler.removeCallbacks(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerInteractor.release()
    }
}