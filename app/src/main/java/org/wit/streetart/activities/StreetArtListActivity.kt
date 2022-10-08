package org.wit.streetart.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.streetart.R
import org.wit.streetart.adapters.StreetArtAdapter
import org.wit.streetart.adapters.StreetArtListener
import org.wit.streetart.databinding.ActivityStreetArtListBinding
import org.wit.streetart.main.MainApp
import org.wit.streetart.models.StreetArtModel


class StreetArtListActivity : AppCompatActivity(), StreetArtListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityStreetArtListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetArtListBinding.inflate(layoutInflater)
        binding.toolbar.title = title
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = StreetArtAdapter(app.streetArts.findAll(), this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, StreetArtActivity::class.java)
                startActivityForResult(launcherIntent,0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStreetArtClick(streetart: StreetArtModel) {
        val launcherIntent = Intent(this, StreetArtActivity::class.java)
        launcherIntent.putExtra("streetart_edit", streetart)
        startActivityForResult(launcherIntent,0)
    }
}


