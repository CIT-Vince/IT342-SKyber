package com.example.skyber.repository
/*
import com.example.skyber.api.RetrofitClient
import com.example.skyber.dataclass.CandidateProfile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CandidateRepository {
    private val api = RetrofitClient.api

    suspend fun getAllCandidates(): Result<List<CandidateProfile>> = try {
        val response = api.getAllCandidates()
        if (response.isSuccessful) {
            Result.success(response.body() ?: emptyList())
        } else {
            Result.failure(Exception("Error: ${response.code()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getCandidateById(id: String): Result<CandidateProfile> = try {
        val response = api.getCandidateById(id)
        if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Candidate not found"))
        } else {
            Result.failure(Exception("Error: ${response.code()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun createCandidateWithImage(
        firstName: String,
        lastName: String,
        age: String?,
        address: String?,
        partylist: String?,
        platform: String?,
        imageFile: File?
    ): Result<CandidateProfile> = try {
        val firstNamePart = firstName.toRequestBody("text/plain".toMediaTypeOrNull())
        val lastNamePart = lastName.toRequestBody("text/plain".toMediaTypeOrNull())
        val agePart = age?.toRequestBody("text/plain".toMediaTypeOrNull())
        val addressPart = address?.toRequestBody("text/plain".toMediaTypeOrNull())
        val partylistPart = partylist?.toRequestBody("text/plain".toMediaTypeOrNull())
        val platformPart = platform?.toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart = imageFile?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", it.name, requestFile)
        }

        val response = api.createCandidateWithImage(
            firstName = firstNamePart,
            lastName = lastNamePart,
            age = agePart,
            address = addressPart,
            partylist = partylistPart,
            platform = platformPart,
            image = imagePart
        )

        if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Failed to create candidate"))
        } else {
            Result.failure(Exception("Error: ${response.code()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateCandidate(id: String, candidate: CandidateProfile): Result<CandidateProfile> = try {
        val response = api.updateCandidate(id, candidate)
        if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Failed to update candidate"))
        } else {
            Result.failure(Exception("Error: ${response.code()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun deleteCandidate(id: String): Result<Unit> = try {
        val response = api.deleteCandidate(id)
        if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Error: ${response.code()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}



 */