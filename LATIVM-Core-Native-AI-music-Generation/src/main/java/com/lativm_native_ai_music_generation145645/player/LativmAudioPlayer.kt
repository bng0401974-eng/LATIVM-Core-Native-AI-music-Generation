package com.lativm_native_ai_music_generation145645.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class LativmAudioPlayer(context: Context) {
    // Use applicationContext to avoid memory leaks
    private val player = ExoPlayer.Builder(context.applicationContext).build()

    fun play(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun stop() {
        player.stop()
    }

    fun release() {
        player.release()
    }
}