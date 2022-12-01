package org.wit.streetart.models

interface StreetArtStore {
    fun findAll(): List<StreetArtModel>
    fun create(streetart: StreetArtModel)
    fun update(streetart: StreetArtModel)
    fun delete(streetart: StreetArtModel)
    fun findById(id:Long) : StreetArtModel?
}