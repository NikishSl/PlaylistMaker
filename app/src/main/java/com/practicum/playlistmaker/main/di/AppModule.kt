package com.practicum.playlistmaker.main.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.main.presentation.MainViewModel
import com.practicum.playlistmaker.player.data.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.AudioPlayerInteractor
import com.practicum.playlistmaker.player.presentation.PlayerViewModel
import com.practicum.playlistmaker.search.data.ITunesApiService
import com.practicum.playlistmaker.search.data.SearchHistoryManager
import com.practicum.playlistmaker.search.presentation.SearchViewModel
import com.practicum.playlistmaker.settings.data.App
import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.domain.SwitchThemeUseCase
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    viewModel { MainViewModel() }

    viewModel { SettingsViewModel(get()) }
    single { App() }
    single { SettingsRepository(androidApplication().getSharedPreferences("ThemePrefs", android.content.Context.MODE_PRIVATE)) }
    single { SwitchThemeUseCase(get()) }

    viewModel { PlayerViewModel(get()) }
    single<AudioPlayerInteractor> { AudioPlayerInteractorImpl { MediaPlayer() } }


    viewModel { SearchViewModel(get()) }
    factory { SearchHistoryManager(get()) }
    single { provideRetrofit() }
    single { provideITunesApiService(get()) }
}

private const val BASE_URL = "https://itunes.apple.com"
fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideITunesApiService(retrofit: Retrofit): ITunesApiService {
    return retrofit.create(ITunesApiService::class.java)
}

