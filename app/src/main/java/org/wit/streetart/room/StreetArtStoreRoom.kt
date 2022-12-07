package org.wit.streetart.room

import android.content.Context
import androidx.room.Room
import org.wit.streetart.models.StreetArtModel
import org.wit.streetart.models.StreetArtStore

class StreetArtStoreRoom(val context: Context) : StreetArtStore {

    var dao: StreetArtDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.streetartDao()
    }

    override suspend fun findAll(): List<StreetArtModel> {
        return dao.findAll()
    }

    override suspend fun clear(){
    }

    override suspend fun findById(id: Long): StreetArtModel? {
        return dao.findById(id)
    }

    override suspend fun create(streetart: StreetArtModel) {
        dao.create(streetart)
    }

    override suspend fun update(streetart: StreetArtModel) {
        dao.update(streetart)
    }

    override suspend fun delete(streetart: StreetArtModel) {
        dao.deletePlacemark(streetart)
    }

}