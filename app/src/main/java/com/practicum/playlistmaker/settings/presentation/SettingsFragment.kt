package com.practicum.playlistmaker.settings.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.data.App
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val app = requireActivity().application as App

        val sharedButton = view.findViewById<ImageButton>(R.id.shared_button)
        val supportButton = view.findViewById<ImageButton>(R.id.support_button)
        val privacyButton = view.findViewById<ImageButton>(R.id.privacy_button)
        val themeSwitcher = view.findViewById<SwitchMaterial>(R.id.themeSwitcher)

        viewModel.darkTheme.observe(viewLifecycleOwner) { isDarkTheme ->
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

        return view
    }
}