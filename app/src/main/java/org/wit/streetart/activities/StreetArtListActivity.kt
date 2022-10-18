package org.wit.streetart.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.streetart.R
import org.wit.streetart.adapters.StreetArtAdapter
import org.wit.streetart.adapters.StreetArtListener
import org.wit.streetart.databinding.ActivityStreetArtListBinding
import org.wit.streetart.main.MainApp
import org.wit.streetart.models.StreetArtModel
import org.wit.streetart.models.StreetArtStore


class StreetArtListActivity : AppCompatActivity(), StreetArtListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityStreetArtListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetArtListBinding.inflate(layoutInflater)
        binding.toolbar.title = title
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadPlacemarks()

        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun loadPlacemarks() {
        showStreetArts(app.streetArts.findAll())
    }

    fun showStreetArts (streetarts: List<StreetArtModel>) {
        binding.recyclerView.adapter = StreetArtAdapter(streetarts, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadPlacemarks() }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, StreetArtActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStreetArtClick(streetart: StreetArtModel) {
        val launcherIntent = Intent(this, StreetArtActivity::class.java)
        launcherIntent.putExtra("streetart_edit", streetart)
        refreshIntentLauncher.launch(launcherIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }
}


