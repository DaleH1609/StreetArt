package org.wit.streetart.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class StreetArtModel(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                          var fbId: String = "",
                          var title: String = "",
                          var description: String = "",
                          var artistName: String = "",
                          var rating: Float = 0f,
                          var image:String = "",
                          @Embedded var location : Location = Location()): Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
