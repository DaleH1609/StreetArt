package org.wit.streetart.views.streetartlist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.streetart.main.MainApp
import org.wit.streetart.models.StreetArtModel
import org.wit.streetart.views.login.LoginView
import org.wit.streetart.views.map.PlacemarkMapView
import org.wit.streetart.views.streetart.StreetArtView

class StreetArtListPresenter(val view: StreetArtListView) {

    var app: MainApp = view.application as MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var editIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    init {
        registerEditCallback()
        registerRefreshCallback()
    }

   suspend fun getPlacemarks() = app.streetArts.findAll()

    fun doLogout(){
        val launcherIntent = Intent(view, LoginView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doAddPlacemark() {
        val launcherIntent = Intent(view, StreetArtView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doEditPlacemark(streetart: StreetArtModel) {
        val launcherIntent = Intent(view, StreetArtView::class.java)
        launcherIntent.putExtra("streetart_edit", streetart)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doShowPlacemarksMap() {
        val launcherIntent = Intent(view, PlacemarkMapView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                GlobalScope.launch(Dispatchers.Main){
                    getPlacemarks()
                }
            }
    }
    private fun registerEditCallback() {
        editIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }

    }
}