package com.example.skyber.api

import com.example.skyber.dataclass.CandidateProfile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface CandidateApi {
    @GET("api/candidates/getAllCandidates")
    suspend fun getAllCandidates(): Response<List<CandidateProfile>>

    @GET("api/candidates/getCandidate/{id}")
    suspend fun getCandidateById(@Path("id") id: String): Response<CandidateProfile>

    // Create operations
    @POST("api/candidates/createCandidate")
    suspend fun createCandidate(@Body candidate: CandidateProfile): Response<CandidateProfile>

    @Multipart
    @POST("api/candidates/createCandidate/with-image")
    suspend fun createCandidateWithImage(
        @Part("firstName") firstName: RequestBody,
        @Part("lastName") lastName: RequestBody,
        @Part("age") age: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("partylist") partylist: RequestBody?,
        @Part("platform") platform: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<CandidateProfile>

    // Update operations
    @PUT("api/candidates/updateCandidate/{id}")
    suspend fun updateCandidate(
        @Path("id") id: String,
        @Body candidate: CandidateProfile
    ): Response<CandidateProfile>

    @Multipart
    @PUT("api/candidates/updateCandidate/{id}/image")
    suspend fun updateCandidateImage(
        @Path("id") id: String,
        @Part image: MultipartBody.Part
    ): Response<CandidateProfile>

    // Delete operation
    @DELETE("api/candidates/deleteCandidate/{id}")
    suspend fun deleteCandidate(@Path("id") id: String): Response<Void>

    // Test endpoint
    @GET("api/candidates/test")
    suspend fun testConnection(): Response<String>
}