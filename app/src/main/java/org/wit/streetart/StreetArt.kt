package org.wit.streetart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import timber.log.Timber
import timber.log.Timber.i

class StreetArt : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streetart)

        Timber.plant(Timber.DebugTree())

        i("Street Art Activity started..")
    }
}