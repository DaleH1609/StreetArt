package org.wit.streetart.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.streetart.main.MainApp

class PlacemarkMapPresenter(val view: PlacemarkMapView) {

  var app: MainApp

  init {
    app = view.application as MainApp
  }

  suspend fun doPopulateMap(map: GoogleMap) {
    map.uiSettings.setZoomControlsEnabled(true)
    map.setOnMarkerClickListener(view)
    app.streetArts.findAll().forEach {
      val loc = LatLng(it.lat, it.lng)
      val options = MarkerOptions().title(it.title).position(loc)
      map.addMarker(options)?.tag = it.id
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
    }
  }

  suspend fun doMarkerSelected(marker: Marker) {
    val tag = marker.tag as Long
    val streetart = app.streetArts.findById(tag)
    if (streetart != null) view.showPlacemark(streetart)
  }
}