package com.lativm_native_ai_music_generation145645.model

import com.google.gson.annotations.SerializedName

data class MusicResponse(
    @SerializedName("audio_url") val audioUrl: String,
    @SerializedName("title") val title: String,
    @SerializedName("status") val status: String
)
