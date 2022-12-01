package org.wit.streetart.views.map


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import org.wit.streetart.databinding.ActivityPlacemarkMapsBinding
import org.wit.streetart.databinding.ContentPlacemarkMapsBinding
import org.wit.streetart.main.MainApp
import org.wit.streetart.models.StreetArtModel

class PlacemarkMapView : AppCompatActivity() , GoogleMap.OnMarkerClickListener{

  private lateinit var binding: ActivityPlacemarkMapsBinding
  private lateinit var contentBinding: ContentPlacemarkMapsBinding
  lateinit var app: MainApp
  lateinit var presenter: PlacemarkMapPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    app = application as MainApp
    binding = ActivityPlacemarkMapsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setSupportActionBar(binding.toolbar)

    presenter = PlacemarkMapPresenter(this)

    contentBinding = ContentPlacemarkMapsBinding.bind(binding.root)

    contentBinding.mapView.onCreate(savedInstanceState)
    contentBinding.mapView.getMapAsync{
      presenter.doPopulateMap(it)
    }
  }
  fun showPlacemark(streetart: StreetArtModel) {
    contentBinding.currentTitle.text = streetart.title
    contentBinding.currentDescription.text = streetart.description
    Picasso.get()
      .load(streetart.image)
      .into(contentBinding.currentImage)
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    presenter.doMarkerSelected(marker)
    return true
  }

  override fun onDestroy() {
    super.onDestroy()
    contentBinding.mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    contentBinding.mapView.onLowMemory()
  }

  override fun onPause() {
    super.onPause()
    contentBinding.mapView.onPause()
  }

  override fun onResume() {
    super.onResume()
    contentBinding.mapView.onResume()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    contentBinding.mapView.onSaveInstanceState(outState)
  }


}