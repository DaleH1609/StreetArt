package org.wit.streetart.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.streetart.activities.MapActivity
import org.wit.streetart.R
import org.wit.streetart.databinding.ActivityStreetartBinding
import org.wit.streetart.helpers.showImagePicker
import org.wit.streetart.main.MainApp
import org.wit.streetart.models.Location
import org.wit.streetart.models.StreetArtModel
import timber.log.Timber.i

private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

class StreetArtActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStreetartBinding
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var streetArt = StreetArtModel()
    lateinit var app : MainApp


    override fun onCreate(savedInstanceState: Bundle?) {
        var edit = false
        registerImagePickerCallback()
        super.onCreate(savedInstanceState)
        binding = ActivityStreetartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp

        binding.placemarkLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }


        if (intent.hasExtra("streetart_edit")) {
            edit = true
            streetArt = intent.extras?.getParcelable("streetart_edit")!!
            binding.streetArtTitle.setText(streetArt.title)
            binding.description.setText(streetArt.description)
            binding.artistName.setText(streetArt.artistName)
            binding.ratingBar.setRating(streetArt.rating.toFloat())
                Picasso.get()
                .load(streetArt.image)
                .into(binding.placemarkImage)
            if(streetArt.image != Uri.EMPTY) {
                binding.chooseImage.setText((R.string.change_placemark_image))
            }
            binding.btnAdd.setText(R.string.save_placemark)
        }

        binding.btnAdd.setOnClickListener() {
            streetArt.title = binding.streetArtTitle.text.toString()
            streetArt.description = binding.description.text.toString()
            streetArt.artistName = binding.artistName.text.toString()
            streetArt.rating = binding.ratingBar.rating.toString()
            if (streetArt.title.isEmpty() && streetArt.description.isEmpty()) {
                Snackbar.make(it,R.string.enter_placemark_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.streetArts.update(streetArt.copy())
                } else {
                    app.streetArts.create(streetArt.copy())
                }
            }
            i("add Button Pressed: $streetArt")
            setResult(RESULT_OK)
            finish()
        }
        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.placemarkLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (streetArt.zoom != 0f) {
                location.lat =  streetArt.lat
                location.lng = streetArt.lng
                location.zoom = streetArt.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                app.streetArts.delete(streetArt)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            streetArt.image = result.data!!.data!!
                            Picasso.get()
                                .load(streetArt.image)
                                .into(binding.placemarkImage)
                            binding.chooseImage.setText((R.string.change_placemark_image))
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            streetArt.lat = location.lat
                            streetArt.lng = location.lng
                            streetArt.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}