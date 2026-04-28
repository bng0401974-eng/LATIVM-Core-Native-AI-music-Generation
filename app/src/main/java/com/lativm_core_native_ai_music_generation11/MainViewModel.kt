package com.lativm_core_native_ai_music_generation11

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lativm_native_ai_music_generation145645.player.LativmAudioPlayer
import com.lativm_core_native_ai_music_generation11.utils.FileUtils
import com.lativm_core_native_ai_music_generation145645.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = MusicRepository()
    private var audioPlayer: LativmAudioPlayer? = null

    // --- НОВО: Држач за текстот од Textbox-от ---
    var userPrompt by mutableStateOf("Hard techno beat, 128 BPM")
        private set

    fun onPromptChange(newPrompt: String) {
        userPrompt = newPrompt
    }
    // ------------------------------------------

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

    fun generateMusic(context: Context) {
        // Сега ја користиме вредноста директно од userPrompt
        val currentPrompt = userPrompt

        viewModelScope.launch {
            _uiState.value = UiState.Loading

            // Овде репозиториумот веќе треба да го пакува ова во MapOf("inputs" to currentPrompt)
            repository.getGeneratedMusic(currentPrompt)
                .onSuccess { responseBody ->
                    val filePath = FileUtils.saveAudioToFile(context, responseBody)
                    if (filePath != null) {
                        _uiState.value = UiState.Success(filePath)
                        // Автоматски пушти го штом се изрендира
                        playGeneratedMusic(context, filePath)
                    } else {
                        _uiState.value = UiState.Error("Грешка при зачувување на аудиото")
                    }
                }
                .onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Непозната грешка")
                }
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