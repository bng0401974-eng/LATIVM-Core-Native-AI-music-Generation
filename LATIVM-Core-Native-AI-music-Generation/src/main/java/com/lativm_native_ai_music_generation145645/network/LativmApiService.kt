package com.lativm_native_ai_music_generation145645.network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface LativmApiService {
    // Го користиме Inference API-то на Hugging Face
    @POST("models/{modelId}")
    suspend fun generateMusic(
        @Path("modelId") modelId: String,
        @Header("Authorization") token: String,
        @Body requestBody: RequestBody
    ): ResponseBody
}
