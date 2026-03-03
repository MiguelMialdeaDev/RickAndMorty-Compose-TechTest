package com.miguelmialdea.rickandmorty

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RickMortyApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}