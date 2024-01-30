package com.practicum.playlistmaker.settings.presentation

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.settings.data.App
import com.practicum.playlistmaker.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val app = applicationContext as App
        viewModel = ViewModelProvider(this, ViewModelFactory(app.switchThemeUseCase)).get(
            SettingsViewModel::class.java)

        val settingsBackButton = findViewById<ImageButton>(R.id.settings_back_button)
        val sharedButton = findViewById<ImageButton>(R.id.shared_button)
        val supportButton = findViewById<ImageButton>(R.id.support_button)
        val privacyButton = findViewById<ImageButton>(R.id.privacy_button)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        viewModel.darkTheme.observe(this) { isDarkTheme ->
            themeSwitcher.isChecked = isDarkTheme
            app.switchTheme(isDarkTheme)
        }

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.setDarkTheme(checked)
        }

        privacyButton.setOnClickListener {
            viewModel.navigateToPrivacyPolicy(this)
        }

        supportButton.setOnClickListener {
            viewModel.navigateToSupport(this)
        }

        sharedButton.setOnClickListener {
            viewModel.shareApp(this)
        }

        settingsBackButton.setOnClickListener {
            finish()
        }
    }
}
