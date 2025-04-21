package com.example.skyber.dataclass

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class VolunteerPost(
    val eventId: String ="",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val location: String = "",
    val eventdate: String = "",
    val contactperson: String = "",
    val contact: String = "",//number
    val email: String = "",
    val status: String = "", //Status should only be Ongoing and Completed
    val requirements: String =""
) : Parcelable