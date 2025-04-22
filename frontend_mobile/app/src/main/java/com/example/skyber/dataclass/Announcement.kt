package com.example.skyber.dataclass

import android.os.Build
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class Announcement(
    val title: String = "",
    val mainContent: String = "",
    val datePosted: String = getCurrentDateTime(),
    val author: String = "",
    val barangay: String = "",
    val category: String = "",
): Parcelable

fun getCurrentDateTime(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(Date())
}