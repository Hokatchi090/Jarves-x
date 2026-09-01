package com.jarvis.assistant

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.Vibrator
import java.io.File

class SecurityGuardModule(private val context: Context) {
    private var recorder: MediaRecorder? = null

    fun triggerPanicMode() {
        // اهتزاز قوي
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(1000)

        // بدء تسجيل صوتي سري
        startRecording()
    }

    private fun startRecording() {
        val outputFile = File(context.getExternalFilesDir(null), "panic_${System.currentTimeMillis()}.mp4")
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }
        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile.absolutePath)
            prepare()
            start()
        }
    }
}
