package com.example.skyber.dataclass

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class Scholarship (
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val link: String ="",
    val contactEmail: String = "",
    val type: String = "",//Private or Public or institution-type Agnostic
    val scholarImage: String? = null
): Parcelable