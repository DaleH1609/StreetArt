package org.wit.streetart.main

import android.app.Application
import org.wit.streetart.models.StreetArtMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val streetArts = StreetArtMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Street Art started")
    }
}