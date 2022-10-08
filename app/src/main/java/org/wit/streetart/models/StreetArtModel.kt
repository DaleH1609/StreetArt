package org.wit.streetart.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StreetArtModel(var id: Long = 0,
                          var title: String = "",
                          var description: String = "") : Parcelable
