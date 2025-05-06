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
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val postedAt: String = getCurrentDateTime(),
    val barangay: String = "",
    val category: String = "",
    val imageData: String? = null, // Base64 string or URL
): Parcelable

fun getCurrentDateTime(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(Date())
}