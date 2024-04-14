package com.practicum.playlistmaker.main.di

import androidx.room.Room
import com.practicum.playlistmaker.db.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java, "app-database"
        ).build()
    }

    single { get<AppDatabase>().favoriteTrackDao() }
}