package com.example.skyber.dataclass

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class JobListing (
    val id: String = "",
    val jobTitle: String = "",
    val companyName: String = "",
    val address: String = "",
    val description: String = "",
    val applicationLink: String = "",
    val employementType: String = "",//Part-Time or Full-Time
    val jobImage: String? = null,
): Parcelable