package com.sunstrinq.ffmpegkitlab.ui.screen

import android.content.Context
import android.util.Log
import androidx.annotation.RawRes
import androidx.lifecycle.ViewModel
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import java.io.File

class HomeViewModel : ViewModel() {

    private var started = false
    private var inputFile: File? = null
    private var outputFile: File? = null

    fun prepareAndCompress(context: Context, @RawRes rawResId: Int) {
        if (started) return
        started = true
        inputFile = copyRawVideoToFilesDir(
            context,
            rawResId,
            "input.mp4"
        )

        outputFile = File(context.filesDir, "output.mp4").apply {
            if (exists()) delete()
        }
        compressVideo(inputFile, outputFile)
    }

    fun copyRawVideoToFilesDir(
        context: Context,
        @RawRes rawResId: Int,
        inputName: String
    ): File {
        val inputFile = File(context.filesDir, inputName)
        if (!inputFile.exists()) {
            context.resources.openRawResource(rawResId).use { input ->
                inputFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
        return inputFile
    }

    private fun compressVideo(inputFile: File?, outputFile: File?) {
        val command = "-y -i ${inputFile?.absolutePath} -vcodec libx264 -crf 28 ${outputFile?.absolutePath}"
        FFmpegKit.executeAsync(command) { session ->
            val returnCode = session.returnCode

            if (ReturnCode.isSuccess(returnCode)) {
                Log.d("FFmpegKit", "Compression success!")
            } else {
                Log.e("FFmpegKit", "Compression failed")
                Log.e("FFmpegKit", session.allLogsAsString)
            }
        }
    }

    override fun onCleared() {
        inputFile?.delete()
        outputFile?.delete()
    }

}