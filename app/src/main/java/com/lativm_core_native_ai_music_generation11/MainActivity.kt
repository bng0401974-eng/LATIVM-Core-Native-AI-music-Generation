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
    // Инстанцирање на ViewModel
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Користиме стандарден MaterialTheme бидејќи специфичната тема не е пронајдена
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
    var promptText by remember { mutableStateOf("") }
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
        TextField(
            value = promptText,
            onValueChange = { promptText = it },
            label = { Text("Внеси опис за музика (пр. Lo-Fi Hip Hop)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.generateMusic(context, promptText) },
            enabled = uiState !is UiState.Loading && promptText.isNotBlank()
        ) {
            Text("Генерирај Беат")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Приказ на состојбата
        when (val state = uiState) {
            is UiState.Loading -> CircularProgressIndicator()
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
                Text("Внеси нешто и притисни на копчето.")
            }
        }
    }
}
