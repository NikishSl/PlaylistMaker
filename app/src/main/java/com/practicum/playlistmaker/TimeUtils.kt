package com.practicum.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

val timeFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

fun formatDuration(durationMillis: Int): String {
    val totalMinutes = durationMillis / 60000
    return String.format(Locale.getDefault(), "%d", totalMinutes)
}

fun formatDurationMinuteWord(durationMillis: Int): String {
    val totalMinutes = durationMillis / 60000
    return changeTextMinuteWithNumb(totalMinutes)
}