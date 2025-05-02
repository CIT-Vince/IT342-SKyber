package com.example.skyber.dataclass

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class CandidateProfile (//Data class for Upcoming SK candidates
    val id: String? = "",
    val firstName: String? = "",
    val lastName: String? = "",
    val age: String? = "",
    val partyList: String? = "",
    val platform: String? = "",
    val address: String? = "",
    val candidateImage: String? = null  // This will store Base64 string
): Parcelable