package com.practicum.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

val timeFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }