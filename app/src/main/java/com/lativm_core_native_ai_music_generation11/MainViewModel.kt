package com.lativm_core_native_ai_music_generation11

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lativm_native_ai_music_generation145645.player.LativmAudioPlayer
import com.lativm_native_ai_music_generation145645.repository.MusicRepository
import com.lativm_core_native_ai_music_generation11.utils.FileUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = MusicRepository()
    private var audioPlayer: LativmAudioPlayer? = null

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    fun playGeneratedMusic(context: Context, url: String) {
        if (audioPlayer == null) {
            audioPlayer = LativmAudioPlayer(context.applicationContext)
        }
        audioPlayer?.play(url)
        _isPlaying.value = true
    }

    fun stopMusic() {
        audioPlayer?.stop()
        _isPlaying.value = false
    }

    fun generateMusic(context: Context, prompt: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getGeneratedMusic(prompt)
                .onSuccess { responseBody ->
                    val filePath = FileUtils.saveAudioToFile(context, responseBody)
                    if (filePath != null) {
                        _uiState.value = UiState.Success(filePath)
                    } else {
                        _uiState.value = UiState.Error("Failed to save audio file")
                    }
                }
                .onFailure { _uiState.value = UiState.Error(it.message ?: "Unknown Error") }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer?.release()
    }
}

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val audioUrl: String) : UiState()
    data class Error(val message: String) : UiState()
}
