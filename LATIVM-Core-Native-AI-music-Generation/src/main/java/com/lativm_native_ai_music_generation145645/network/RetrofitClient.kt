package com.lativm_native_ai_music_generation145645.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api-inference.huggingface.co/"
    const val HF_TOKEN = "Bearer hf_xxxxxxxxxxxxxxxxxxxx" // Твојот клуч тука
    const val MODEL_ID = "facebook/musicgen-small"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val instance: LativmApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        retrofit.create(LativmApiService::class.java)
    }
}
