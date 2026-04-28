package com.lativm_core_native_ai_music_generation145645.network

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LativmApiService {
    @POST("run/predict")
    suspend fun generateMusic(
        @Header("Authorization") token: String,
        @Body request: GradioRequest
    ): ResponseBody
}