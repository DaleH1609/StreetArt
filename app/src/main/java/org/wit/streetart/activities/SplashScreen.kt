package org.wit.streetart.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import org.wit.streetart.R
import org.wit.streetart.views.streetart.StreetArtView
import org.wit.streetart.views.streetartlist.StreetArtListView

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        supportActionBar?.hide()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this@SplashScreen, StreetArtListView::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}