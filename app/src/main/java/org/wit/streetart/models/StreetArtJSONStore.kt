package org.wit.streetart.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.placemark.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "streetarts.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<StreetArtModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class StreetArtJSONStore(private val context: Context) : StreetArtStore {

    var streetArts = mutableListOf<StreetArtModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<StreetArtModel> {
        logAll()
        return streetArts
    }

    override fun create(streetart: StreetArtModel) {
        streetart.id = generateRandomId()
        streetArts.add(streetart)
        serialize()
    }


    override fun update(streetart: StreetArtModel) {
        var foundStreetArt: StreetArtModel? = streetArts.find { p -> p.id == streetart.id }
        if (foundStreetArt != null) {
            foundStreetArt.title = streetart.title
            foundStreetArt.description = streetart.description
            foundStreetArt.image = streetart.image
            foundStreetArt.lat = streetart.lat
            foundStreetArt.lng = streetart.lng
            foundStreetArt.zoom = streetart.zoom
            serialize()
            logAll()
        }
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(streetArts, listType)
        write(context, JSON_FILE, jsonString)
    }

    override fun delete(placemark: StreetArtModel) {
        streetArts.remove(placemark)
        serialize()
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        streetArts = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        streetArts.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}