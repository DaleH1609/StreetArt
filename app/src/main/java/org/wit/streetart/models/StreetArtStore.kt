package org.wit.streetart.models

interface StreetArtStore {
    suspend fun findAll(): List<StreetArtModel>
    suspend fun create(streetart: StreetArtModel)
    suspend fun update(streetart: StreetArtModel)
    suspend fun delete(streetart: StreetArtModel)
    suspend fun findById(id:Long) : StreetArtModel?
    suspend fun clear()
}