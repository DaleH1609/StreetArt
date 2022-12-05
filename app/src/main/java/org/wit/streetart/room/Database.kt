package org.wit.streetart.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.wit.streetart.helpers.Converters
import org.wit.streetart.models.StreetArtModel

@Database(entities = [StreetArtModel::class], version = 2,  exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun streetartDao(): StreetArtDao
}