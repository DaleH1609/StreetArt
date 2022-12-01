package org.wit.streetart.views.streetartlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.streetart.R
import org.wit.streetart.adapters.StreetArtAdapter
import org.wit.streetart.adapters.StreetArtListener
import org.wit.streetart.databinding.ActivityStreetArtListBinding
import org.wit.streetart.main.MainApp
import org.wit.streetart.models.StreetArtModel
import timber.log.Timber


class StreetArtListView :  AppCompatActivity(), StreetArtListener {

    lateinit var app: MainApp
    lateinit var presenter: StreetArtListPresenter
    lateinit var binding: ActivityStreetArtListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.i("Recycler View Loaded")
        super.onCreate(savedInstanceState)
        binding = ActivityStreetArtListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = StreetArtListPresenter(this)
        //app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter =
            StreetArtAdapter(presenter.getPlacemarks(), this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        //update the view
        binding.recyclerView.adapter?.notifyDataSetChanged()
        Timber.i("recyclerView onResume")
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_add -> presenter.doAddPlacemark()
            R.id.item_map -> presenter.doShowPlacemarksMap()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStreetArtClick(streetart: StreetArtModel) {
        presenter.doEditPlacemark(streetart)
    }
}