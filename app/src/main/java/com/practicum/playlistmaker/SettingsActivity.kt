package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val settingsBackButton = findViewById<ImageButton>(R.id.settings_back_button)
        val sharedButton = findViewById<ImageButton>(R.id.shared_button)
        val supprotButton = findViewById<ImageButton>(R.id.support_button)
        val privacyButton = findViewById<ImageButton>(R.id.privacy_button)

        privacyButton.setOnClickListener {
            privacyPolicy()
        }

        supprotButton.setOnClickListener {
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
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/profile/android-developer")

        startActivity(Intent.createChooser(shareIntent, "Поделиться приложением"))
    }

    private fun contactSupport() {
        val email = "email"
        val subject = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
        val message = "Спасибо разработчикам и разработчицам за крутое приложение!"

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$email")
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)

        try {
            startActivity(Intent.createChooser(intent, "Выберите почтовый клиент"))
            } catch (e: Exception) {
        }
    }

    private fun privacyPolicy() {
        val url = "https://yandex.ru/legal/practicum_offer"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        try {
            startActivity(intent)
            } catch (e: Exception) {
        }
    }
}