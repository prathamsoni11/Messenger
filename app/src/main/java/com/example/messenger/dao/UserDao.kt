package com.example.messenger.dao

import com.example.messenger.models.User
import com.google.firebase.database.FirebaseDatabase

class UserDao {
    private val database = FirebaseDatabase.getInstance()
    val userCollection = database.reference.child("users")

    fun addUser(user: User){
        userCollection.child(user.uid).setValue(user)
    }
}