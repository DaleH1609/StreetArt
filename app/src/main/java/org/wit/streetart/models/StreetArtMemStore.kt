package org.wit.streetart.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class StreetArtMemStore : StreetArtStore {

    val streetarts = ArrayList<StreetArtModel>()

    override fun findAll(): List<StreetArtModel> {
        return streetarts
    }

    override fun create(streetart: StreetArtModel) {
        streetart.id
        streetarts.add(streetart)
        logAll()
    }

    override fun delete(streetart: StreetArtModel) {
        streetart.id
        streetarts.remove(streetart)
        logAll()
    }

    fun logAll() {
        streetarts.forEach { i("${it}") }
    }

    override fun update(streetart: StreetArtModel) {
        var foundStreetArt: StreetArtModel? = streetarts.find { p -> p.id == streetart.id }
        if (foundStreetArt != null) {
            foundStreetArt.title = streetart.title
            foundStreetArt.description = streetart.description
            foundStreetArt.image = streetart.image
            logAll()
        }
    }
}

