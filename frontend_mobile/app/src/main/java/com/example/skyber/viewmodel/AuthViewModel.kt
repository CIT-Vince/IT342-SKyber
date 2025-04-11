package com.example.skyber.viewmodel


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.skyber.model.UserInfo
import com.example.skyber.network.ApiClient
import com.example.skyber.network.UserApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {
    private val apiService = ApiClient.retrofit.create(UserApiService::class.java)

    var registerResult = mutableStateOf<String?>(null)
        private set

    fun registerUser(user: UserInfo) {
        val call = apiService.registerUser(user)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                registerResult.value = response.body() ?: "Registration failed"
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                registerResult.value = "Error: ${t.message}"
            }
        })
    }
}