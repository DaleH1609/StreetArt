package org.wit.streetart.views.streetart

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.streetart.helpers.checkLocationPermissions
import org.wit.streetart.helpers.createDefaultLocationRequest
import org.wit.streetart.helpers.showImagePicker
import org.wit.streetart.main.MainApp
import org.wit.streetart.models.Location
import org.wit.streetart.models.StreetArtModel
import org.wit.streetart.views.editlocation.EditLocationView
import timber.log.Timber


class StreetArtPresenter(private val view: StreetArtView){
    private val locationRequest = createDefaultLocationRequest()
    var map: GoogleMap? = null
    var streetart = StreetArtModel()
    var app: MainApp = view.application as MainApp
    //location service
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var edit = false;
    private val location = Location(52.245696, -7.139102, 15f)

    init {

        doPermissionLauncher()
        registerImagePickerCallback()
        registerMapCallback()

        if (view.intent.hasExtra("streetart_edit")) {
            edit = true
            streetart = view.intent.extras?.getParcelable("streetart_edit")!!
            view.showPlacemark(streetart)
        }
        else {

            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
            streetart.location.lat = location.lat
            streetart.location.lng = location.lng
        }

    }


    suspend fun doAddOrSave(title: String, description: String, artistName: String) {
        streetart.title = title
        streetart.description = description
        streetart.artistName = artistName
        if (edit) {
            app.streetArts.update(streetart)
        } else {
            app.streetArts.create(streetart)
        }

        view.finish()

    }

    fun doCancel() {
        view.finish()
    }

   suspend fun doDelete() {
        app.streetArts.delete(streetart)
        view.finish()
    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher)
    }

    fun doSetLocation() {

        if (streetart.location.zoom != 0f) {
            location.lat =  streetart.location.lat
            location.lng = streetart.location.lng
            location.zoom = streetart.location.zoom
            locationUpdate(streetart.location.lat, streetart.location.lng)
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {

        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }
    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(streetart.location.lat, streetart.location.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {
        streetart.location = location
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(streetart.title).position(LatLng(streetart.location.lat, streetart.location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(streetart.location.lat, streetart.location.lng), streetart.location.zoom))
        view.showPlacemark(streetart)
    }

    fun cachePlacemark (title: String, description: String) {
        streetart.title = title;
        streetart.description = description
    }

    private fun registerImagePickerCallback() {

        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            streetart.image = result.data!!.data!!
                            view.updateImage(streetart.image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            streetart.location = location
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }

    private fun doPermissionLauncher() {
        Timber.i("permission check called")
        requestPermissionLauncher =
            view.registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                if (isGranted) {
                    doSetCurrentLocation()
                } else {
                    locationUpdate(location.lat, location.lng)
                }
            }
    }
}