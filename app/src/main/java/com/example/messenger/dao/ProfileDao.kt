package com.example.messenger.dao

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ProfileDao {
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val profileCollection = storage.reference.child("ProfilesPictures")

    suspend fun addProfilePicture(selectedImage: Uri): Uri {
        val reference = profileCollection.child(auth.uid.toString())
        reference.putFile(selectedImage).await()
        return reference.downloadUrl.await()
    }

}