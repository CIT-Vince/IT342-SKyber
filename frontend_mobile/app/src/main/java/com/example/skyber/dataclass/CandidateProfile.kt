package com.example.skyber.dataclass

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class CandidateProfile (
    val id: String? = "",
    val firstName: String? = "",
    val lastName: String? = "",
    //val status: String? = "",
    //val email: String? = "",
    val age: String? = "",
    val partyList: String? = "",
    val platform: String? = "",
    val address: String? = "",
    //Image URL here
): Parcelable