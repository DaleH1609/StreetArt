package org.wit.streetart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.streetart.databinding.CardPlacemarkBinding
import org.wit.streetart.models.StreetArtModel

interface StreetArtListener {
    fun onStreetArtClick(streetart: StreetArtModel)
}

class StreetArtAdapter constructor(private var streetarts: List<StreetArtModel>, private val listener: StreetArtListener) :
    RecyclerView.Adapter<StreetArtAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPlacemarkBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val streetart = streetarts[holder.adapterPosition]
        holder.bind(streetart, listener)
    }

    override fun getItemCount(): Int = streetarts.size

    class MainHolder(private val binding: CardPlacemarkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(streetart: StreetArtModel, listener: StreetArtListener) {
            binding.streetArtTitle.text = streetart.title
            binding.description.text = streetart.description
            binding.root.setOnClickListener { listener.onStreetArtClick(streetart) }
        }
    } }