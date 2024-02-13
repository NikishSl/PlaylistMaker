package com.practicum.playlistmaker.media.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaActivity : AppCompatActivity() {

    private val viewModel: MediaViewModel by viewModel()
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val mediaBackButton = findViewById<ImageButton>(R.id.media_back_button)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        val fragmentList = listOf(FavoritesFragment(), PlaylistFragment())
        val adapter = MediaPagerAdapter(fragmentList, supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        viewModel.tabTitles.observe(this, Observer { titles ->
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = titles[position]
            }.attach()
        })

        mediaBackButton.setOnClickListener {
            finish()
        }
    }
}