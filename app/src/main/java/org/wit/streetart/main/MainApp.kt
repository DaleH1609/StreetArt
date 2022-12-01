package org.wit.streetart.main

import android.app.Application
import org.wit.streetart.models.*
import org.wit.streetart.room.StreetArtStoreRoom
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var streetArts: StreetArtStore
    lateinit var users: UserStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        streetArts = StreetArtStoreRoom(applicationContext)
        users = UserJSONStore(applicationContext)
        i("Street Art started")
    }
}