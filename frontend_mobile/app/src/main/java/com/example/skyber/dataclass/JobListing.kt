package com.example.skyber.dataclass

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class JobListing (
    val jobtitle: String = "",
    val companyname: String = "",
    val description: String = "",
    val contact: String = "",
    val location: String = "",
    val contactperson: String = "",
    val address: String = "",
    val category: String = "",//Part-Time or Full-Time
): Parcelable