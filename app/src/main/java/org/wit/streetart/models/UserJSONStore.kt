package org.wit.streetart.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.placemark.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val UJSON_FILE = "user.json"
val UgsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UserJSONStore.UriParser())
    .create()

val UlistType: Type = object : TypeToken<ArrayList<UserModel>>() {}.type

fun generateRandomUId(): Long {
    return Random().nextLong()
}

class UserJSONStore(private val context: Context) : UserStore {

    var users = mutableListOf<UserModel>()

    init {
        if (exists(context,UJSON_FILE)) {
            deserialize()
        }
    }

    private fun logAll() {
        users.forEach { Timber.i("$it") }
    }

    private fun deserialize() {
        val jsonString = read(context, UJSON_FILE)
        users = UgsonBuilder.fromJson(jsonString, UlistType)
    }

    private fun serialize() {
        val jsonString = UgsonBuilder.toJson(users, UlistType)
        write(context, UJSON_FILE, jsonString)
    }

    override fun findAll(): MutableList<UserModel> {
        logAll()
        return users
    }

    override fun create(user: UserModel) {
        user.id = generateRandomUId()
        users.add(user)
        serialize()
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
}


