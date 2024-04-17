package com.practicum.playlistmaker
import android.content.Context
import android.util.TypedValue
import android.view.View

fun dpToPx(context: Context, dp: Float): Int {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics).toInt()
}

fun dpToPxView(view: View, dp: Float): Int {
    val metrics = view.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics).toInt()
}