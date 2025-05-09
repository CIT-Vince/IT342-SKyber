package com.example.skyber.dataclass

data class User(
    val id: String? = null,

    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val password: String? = null,

    val age: Int? = null,
    val birthdate: String? = null,

    val gender: String? = null,
    val role: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val volunteeredActivities: List<String> = emptyList(),

)


