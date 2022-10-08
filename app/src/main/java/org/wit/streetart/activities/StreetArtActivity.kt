package org.wit.streetart.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import org.wit.streetart.R
import org.wit.streetart.databinding.ActivityStreetartBinding
import org.wit.streetart.main.MainApp
import org.wit.streetart.models.StreetArtModel
import timber.log.Timber.i

class StreetArtActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStreetartBinding
    var streetArt = StreetArtModel()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Placemark Activity started...")
        if (intent.hasExtra("streetart_edit")) {
            streetArt = intent.extras?.getParcelable("placemark_edit")!!
            binding.streetArtTitle.setText(streetArt.title)
            binding.description.setText(streetArt.description)
        }

        binding.btnAdd.setOnClickListener() {
            streetArt.title = binding.streetArtTitle.text.toString()
            streetArt.description = binding.description.text.toString()
            if (streetArt.title.isNotEmpty()) {
                app.streetArts.create(streetArt.copy())
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar
                    .make(it, "Please enter a title for your art", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}