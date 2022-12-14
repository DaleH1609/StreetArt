package org.wit.streetart.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class StreetArtMemStore : StreetArtStore {

    val streetarts = ArrayList<StreetArtModel>()

    override suspend fun findAll(): List<StreetArtModel> {
        return streetarts
    }

    override suspend fun create(streetart: StreetArtModel) {
        streetart.id
        streetarts.add(streetart)
        logAll()
    }

    override suspend fun findById(id:Long) : StreetArtModel? {
        val foundPlacemark: StreetArtModel? = streetarts.find { it.id == id }
        return foundPlacemark
    }

    override suspend fun delete(streetart: StreetArtModel) {
        streetarts.remove(streetart)
    }

    override suspend fun clear(){
        streetarts.clear()
    }

    fun logAll() {
        streetarts.forEach { i("${it}") }
    }

    override suspend fun update(streetart: StreetArtModel) {
        var foundStreetArt: StreetArtModel? = streetarts.find { p -> p.id == streetart.id }
        if (foundStreetArt != null) {
            foundStreetArt.title = streetart.title
            foundStreetArt.description = streetart.description
            foundStreetArt.artistName = streetart.artistName
            foundStreetArt.image = streetart.image
            foundStreetArt.location = streetart.location
            logAll()
        }
    }
}

