package com.lativm_core_native_ai_music_generation145645.repository

import com.lativm_core_native_ai_music_generation145645.network.RetrofitClient
import com.lativm_core_native_ai_music_generation145645.network.GradioRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

class MusicRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getGeneratedMusic(prompt: String): Result<ResponseBody> {
        return withContext(Dispatchers.IO) {
            try {
                // Мора да има точно 3 параметри: prompt, duration, loop_mode
                val request = GradioRequest(
                    data = listOf(
                        prompt,      // Текстот
                        15,          // Времетраење (Int)
                        "Normal"     // Мод (String)
                    )
                )

                val responseBody = apiService.generateMusic(
                    RetrofitClient.HF_TOKEN,
                    request
                )
                Result.success(responseBody)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}