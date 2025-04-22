package com.example.skyber

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
//Singleton file para instantiate sa firebase realtime db
object FirebaseHelper {
    val databaseInstance: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    val databaseReference: DatabaseReference by lazy {
        databaseInstance.reference
    }

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
}
