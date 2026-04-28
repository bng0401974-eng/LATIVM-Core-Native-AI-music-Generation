package com.lativm_core_native_ai_music_generation11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MusicGeneratorScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun PulseAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .size(40.dp)
            .scale(scale)
            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
    )
}

@Composable
fun MusicGeneratorScreen(viewModel: MainViewModel) {
    // Ги користиме вредностите директно од ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ТЕКСТ ПОЛЕ ПОВРЗАНО СО VIEWMODEL
        OutlinedTextField(
            value = viewModel.userPrompt,
            onValueChange = { viewModel.onPromptChange(it) },
            label = { Text("Внеси опис за музика (пр. Lo-Fi Hip Hop)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is UiState.Loading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // КОПЧЕ СО ЕДЕН АРГУМЕНТ (ПОПРАВЕНО)
        Button(
            onClick = { viewModel.generateMusic(context) },
            enabled = uiState !is UiState.Loading && viewModel.userPrompt.isNotBlank()
        ) {
            Text("Генерирај Беат")
        }

        Spacer(modifier = Modifier.height(32.dp))

        when (val state = uiState) {
            is UiState.Loading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Машината рендира...")
                }
            }
            is UiState.Success -> {
                Card(
                    modifier = Modifier.padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Беатот е подготвен!", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        if (isPlaying) {
                            PulseAnimation()
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(onClick = { viewModel.playGeneratedMusic(context, state.audioUrl) }) {
                                Text("PUSTI")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(onClick = { viewModel.stopMusic() }) {
                                Text("STOP")
                            }
                        }
                    }
                }
            }
            is UiState.Error -> {
                Text("Грешка: ${state.message}", color = MaterialTheme.colorScheme.error)
            }
            is UiState.Idle -> {
                Text("Внеси инструкција и притисни на копчето.")
            }
        }
    }
}