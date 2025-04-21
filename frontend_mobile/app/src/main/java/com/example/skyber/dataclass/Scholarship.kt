package com.example.skyber.dataclass

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class Scholarship (
    val title: String = "",
    val description: String = "",
    val contactemail: String = "",
    val applicationlink: String ="",
    val category: String = "",//Part-Time or Full-Time
): Parcelable