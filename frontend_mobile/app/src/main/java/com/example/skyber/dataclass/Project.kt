package com.example.skyber.dataclass

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class Project(
    val id: String = "",
    val projectName: String = "",
    val projectDescription: String = "",
    val status: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val budget: String = "",
    val projectManager: String = "",
    val projectMembers: List<String> = emptyList(),
    val stakeholders: List<String> = emptyList(),
    val sustainabilityGoals: String = "",
): Parcelable