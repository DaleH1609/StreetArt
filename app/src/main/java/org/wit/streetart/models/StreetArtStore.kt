package org.wit.streetart.models

interface StreetArtStore {
    fun findAll(): List<StreetArtModel>
    fun create(streetart: StreetArtModel)
    fun update(placemark: StreetArtModel)
}