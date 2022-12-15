package org.wit.streetart.models

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.wit.streetart.helpers.readImageFromPath
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File

class StreetArtFireStore(val context: Context) : StreetArtStore {
    val streetarts = ArrayList<StreetArtModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference
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
            updateImage(streetart)
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
        if(streetart.image.length > 0){
            updateImage(streetart)
        }

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
        st = FirebaseStorage.getInstance().reference
        db = FirebaseDatabase.getInstance("https://streetart-d49d5-default-rtdb.firebaseio.com").reference
        streetarts.clear()
        db.child("users").child(userId).child("streetarts")
            .addListenerForSingleValueEvent(valueEventListener)
    }

    fun updateImage(streetart: StreetArtModel) {
        if (streetart.image != "") {
            val fileName = File(streetart.image)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, streetart.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        streetart.image = it.toString()
                        db.child("users").child(userId).child("streetarts").child(streetart.fbId).setValue(streetart)
                    }
                }.addOnFailureListener{
                    var errorMessage = it.message
                    Timber.i("Failure: $errorMessage")
                }
            }
        }
    }
}
