package com.lativm_core_native_ai_music_generation145645.repository

import com.lativm_core_native_ai_music_generation145645.network.RetrofitClient
import com.lativm_core_native_ai_music_generation145645.network.GradioRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import android.util.Log

class MusicRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getGeneratedMusic(prompt: String): Result<ResponseBody> {
        return withContext(Dispatchers.IO) {
            try {
                // МОРА да ги пратиш сите 3 параметри што ги бара Python функцијата:
                // 1. prompt (String)
                // 2. duration (Number - во Python е default 10)
                // 3. loop_mode (String - "Normal" или "Loop Ready (Seamless)")

                // Во MusicRepository.kt
                // ВО MusicRepository.kt - Мора да има 3 елементи во листата!
                val request = GradioRequest(
                    data = listOf(
                        prompt,      // 1. Text input
                        15,          // 2. Duration (Slider)
                        "Normal"     // 3. Loop Mode (Radio)
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