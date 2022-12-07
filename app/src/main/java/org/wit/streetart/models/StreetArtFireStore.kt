package org.wit.streetart.models

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class StreetArtFireStore(val context: Context) : StreetArtStore {
    val streetarts = ArrayList<StreetArtModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    override suspend fun findAll(): List<StreetArtModel> {
        return streetarts
    }

    override suspend fun findById(id: Long): StreetArtModel? {
        val foundStreetArt: StreetArtModel? = streetarts.find { p -> p.id == id }
        return foundStreetArt
    }

    override suspend fun create(streetart: StreetArtModel) {
        val key = db.child("users").child(userId).child("streetarts").push().key
        key?.let {
            streetart.fbId = key
            streetarts.add(streetart)
            db.child("users").child(userId).child("streetarts").child(key).setValue(streetart)
        }
    }

    override suspend fun update(streetart: StreetArtModel) {
        var foundStreetArt: StreetArtModel? = streetarts.find { p -> p.fbId == streetart.fbId }
        if (foundStreetArt != null) {
            foundStreetArt.title = streetart.title
            foundStreetArt.description = streetart.description
            foundStreetArt.artistName = streetart.artistName
            foundStreetArt.image = streetart.image
            foundStreetArt.location = streetart.location
        }

        db.child("users").child(userId).child("streetarts").child(streetart.fbId).setValue(streetart)

    }

    override suspend fun delete(streetart: StreetArtModel) {
        db.child("users").child(userId).child("streetarts").child(streetart.fbId).removeValue()
        streetarts.remove(streetart)
    }

    override suspend fun clear() {
        streetarts.clear()
    }

    fun fetchStreetArts(streetartsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(streetarts) {
                    it.getValue<StreetArtModel>(
                        StreetArtModel::class.java
                    )
                }
                streetartsReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance("https://streetart-e24c4-default-rtdb.europe-west1.firebasedatabase.app").reference
        streetarts.clear()
        db.child("users").child(userId).child("streetarts")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}