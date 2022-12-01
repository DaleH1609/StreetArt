package org.wit.streetart.room

import androidx.room.*
import org.wit.streetart.models.StreetArtModel

@Dao
interface StreetArtDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun create(streetart: StreetArtModel)

    @Query("SELECT * FROM StreetArtModel")
    suspend fun findAll(): List<StreetArtModel>

    @Query("select * from StreetArtModel where id = :id")
    suspend fun findById(id: Long): StreetArtModel

    @Update
    suspend fun update(streetart: StreetArtModel)

    @Delete
    suspend fun deletePlacemark(streetart: StreetArtModel)
}