package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    companion object {
        private const val EXAMPLE_EMAIL = "email@example.com"
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val settingsBackButton = findViewById<ImageButton>(R.id.settings_back_button)
        val sharedButton = findViewById<ImageButton>(R.id.shared_button)
        val supportButton = findViewById<ImageButton>(R.id.support_button)
        val privacyButton = findViewById<ImageButton>(R.id.privacy_button)

        privacyButton.setOnClickListener {
            privacyPolicy()
        }

        supportButton.setOnClickListener {
            contactSupport()
        }

        sharedButton.setOnClickListener {
            shareApp()
        }

        settingsBackButton.setOnClickListener {
            finish()
        }
    }

    private fun shareApp(){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))

        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app_chooser_title)))
    }

    private fun contactSupport() {
        val email = EXAMPLE_EMAIL
        val subject = getString(R.string.share_app_subject)
        val message = getString(R.string.share_app_message)


        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$email")
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)

        try {
            startActivity(Intent.createChooser(intent, getString(R.string.choose_email_client)))
            } catch (e: Exception) {
        }
    }

    private fun privacyPolicy() {
        val url = getString(R.string.privacy_policy_url)

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        try {
            startActivity(intent)
            } catch (e: Exception) {
        }
    }
}