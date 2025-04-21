package com.example.skyber.dataclass

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class CandidateProfile (
    val candidateId: String? = "",
    val firstname: String? = "",
    val lastname: String? = "",
    //val status: String? = "",
    val email: String? = "",
    val age: String? = "",
    val partylist: String? = "",
    val platform: String? = "",
    val address: String? = "",
    ): Parcelable