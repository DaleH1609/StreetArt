package org.wit.streetart.main

import android.app.Application
import org.wit.streetart.models.StreetArtJSONStore
import org.wit.streetart.models.StreetArtMemStore
import org.wit.streetart.models.StreetArtStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var streetArts: StreetArtStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        streetArts = StreetArtJSONStore(applicationContext)
        i("Street Art started")
    }
}