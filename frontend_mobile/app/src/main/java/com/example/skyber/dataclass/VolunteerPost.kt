package com.example.skyber.dataclass

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class VolunteerPost(
    val id: String ="",
    val title: String = "",
    val description: String = "",
    val registerLink: String = "",
    val category: String = "",
    val location: String = "",
    val eventDate: String = "",
    val contactPerson: String = "",
    val contactEmail: String = "",//email
    val status: String = "", //Status should only be Ongoing and Completed
    val requirements: String ="",
    //val VolunteeredUsers: List<User>? = null
) : Parcelable