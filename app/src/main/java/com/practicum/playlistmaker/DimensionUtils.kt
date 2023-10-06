package com.practicum.playlistmaker
import android.content.Context
import android.util.TypedValue

fun dpToPx(context: Context, dp: Float): Int {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics).toInt()
}