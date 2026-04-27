package com.lativm_native_ai_music_generation145645.repository

import com.lativm_native_ai_music_generation145645.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject

class MusicRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getGeneratedMusic(prompt: String): Result<ResponseBody> {
        return withContext(Dispatchers.IO) {
            try {
                val json = JSONObject()
                json.put("inputs", prompt)
                val requestBody = json.toString().toRequestBody("application/json".toMediaType())

                val response = apiService.generateMusic(
                    RetrofitClient.MODEL_ID,
                    RetrofitClient.HF_TOKEN,
                    requestBody
                )
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
