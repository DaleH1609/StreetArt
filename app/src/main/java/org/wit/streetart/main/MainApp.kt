package org.wit.streetart.main

import android.app.Application
import org.wit.streetart.models.*
import org.wit.streetart.room.StreetArtStoreRoom
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var streetArts: StreetArtStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        streetArts = StreetArtFireStore(applicationContext)
        i("Street Art started")
    }
}