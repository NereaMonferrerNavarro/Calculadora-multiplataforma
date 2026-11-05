package com.example.calculadoramultiplataforma

import android.media.AudioManager
import android.media.ToneGenerator

private val tono by lazy { ToneGenerator(AudioManager.STREAM_MUSIC, 65) }

actual fun pitido() {
    tono.startTone(ToneGenerator.TONE_PROP_BEEP, 80)
}