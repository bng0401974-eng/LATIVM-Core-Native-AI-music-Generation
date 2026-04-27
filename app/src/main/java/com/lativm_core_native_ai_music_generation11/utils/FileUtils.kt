package com.lativm_core_native_ai_music_generation11.utils

import android.content.Context
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

object FileUtils {
    fun saveAudioToFile(context: Context, body: ResponseBody): String? {
        return try {
            val file = File(context.cacheDir, "generated_music.mp3")
            val inputStream = body.byteStream()
            val outputStream = FileOutputStream(file)
            
            val buffer = ByteArray(4096)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            
            outputStream.flush()
            outputStream.close()
            inputStream.close()
            
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
