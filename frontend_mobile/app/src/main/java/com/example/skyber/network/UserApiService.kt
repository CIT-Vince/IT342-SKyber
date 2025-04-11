package com.example.skyber.network

import com.example.skyber.model.UserInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {
    @POST("auth/addNewUser")
    fun registerUser(@Body user: UserInfo): Call<String>
}