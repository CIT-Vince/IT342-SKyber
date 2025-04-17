package com.example.skyber.dataclass

import android.os.Build
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Announcement(
    //val id: String = UUID.randomUUID().toString(), optional
    val title: String,
    val mainContent: String,
    val datePosted: String = getCurrentDateTime(),
    val author: String
)

fun getCurrentDateTime(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(Date())
}