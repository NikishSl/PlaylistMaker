package com.practicum.playlistmaker.settings.presentation

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.SwitchThemeUseCase

class SettingsViewModel(private val switchThemeUseCase: SwitchThemeUseCase) : ViewModel() {

    companion object {
        private const val EXAMPLE_EMAIL = "email@example.com"
    }

    private val _darkTheme = MutableLiveData<Boolean>()
    val darkTheme: LiveData<Boolean> = _darkTheme

    init {
        _darkTheme.value = switchThemeUseCase.isDarkThemeEnabled()
    }

    fun setDarkTheme(isDarkTheme: Boolean) {
        switchThemeUseCase.execute(isDarkTheme)
        _darkTheme.value = isDarkTheme
    }

    fun navigateToPrivacyPolicy(activity: AppCompatActivity) {
        val url = activity.getString(R.string.privacy_policy_url)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        try {
            activity.startActivity(intent)
        } catch (e: Exception) {
        }
    }

    fun navigateToSupport(activity: AppCompatActivity) {
        val email = EXAMPLE_EMAIL
        val subject = activity.getString(R.string.share_app_subject)
        val message = activity.getString(R.string.share_app_message)

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$email")
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)

        try {
            activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.choose_email_client)))
        } catch (e: Exception) {
        }
    }

    fun shareApp(activity: AppCompatActivity) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.share_text))
        activity.startActivity(Intent.createChooser(shareIntent, activity.getString(R.string.share_app_chooser_title)))
    }
}