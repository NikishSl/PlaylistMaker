package com.practicum.playlistmaker.player.presentation

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.player.domain.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.Track

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var audioPlayerInteractor: AudioPlayerInteractor
    private val _track = MutableLiveData<Track>()
    val track: LiveData<Track>
        get() = _track
    private val _trackTime = MutableLiveData<Int>()
    val trackTime: LiveData<Int>
        get() = _trackTime

    private var trackTimeUpdater: Runnable? = null
    private val handler = Handler(Looper.getMainLooper())

    init {
        audioPlayerInteractor = AudioPlayerInteractorImpl()
    }

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
            handler.postDelayed(trackTimeUpdater!!, 1000)
        }
        handler.post(trackTimeUpdater!!)
    }

    fun stopTrackTimeUpdates() {
        trackTimeUpdater?.let {
            handler.removeCallbacks(it)
        }
    }

    fun getAudioPlayerInteractor(): AudioPlayerInteractor {
        return audioPlayerInteractor
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerInteractor.release()
    }
}