package com.example.messenger.dao

import com.example.messenger.models.User
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    private val database = FirebaseDatabase.getInstance()
    private val userCollection = database.reference.child("users")

    fun addUser(user: User?){
        user?.let {
            GlobalScope.launch(Dispatchers.IO){
                userCollection.child(user.uid).setValue(user)
            }
        }
    }
}