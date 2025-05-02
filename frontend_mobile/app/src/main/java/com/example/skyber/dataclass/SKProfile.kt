package com.example.skyber.dataclass

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class SKProfile(
    val uid: String? = "",
    val firstName: String? = "",
    val lastName: String? = "",
    val email: String? = "",
    val position: String? = "",
    val term: String? = "",       // e.g., "2015-2018"
    val platform: String? = "",
    val birthdate: String? = "",
    val gender: String? = "",
    val age: Int = 0,
    val phoneNumber: String? = "",
    val address: String? = "",
    val skImage: String? = null, // Base64 string or URL
    val role: String = "ADMIN"   // Default role
) : Parcelable