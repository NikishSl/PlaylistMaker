package com.practicum.playlistmaker

fun changeTextTrack(number: Int): String {
    return when {
        number % 100 in 11..19 -> "треков"
        number % 10 == 1 -> "трек"
        number % 10 in 2..4 -> "трека"
        else -> "треков"
    }
}

fun changeTextTrackWithNumb(number: Int): String {
    return when {
        number % 100 in 11..19 -> "$number треков"
        number % 10 == 1 -> "$number трек"
        number % 10 in 2..4 -> "$number трека"
        else -> "$number треков"
    }
}