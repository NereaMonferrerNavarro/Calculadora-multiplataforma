package com.example.calculadoramultiplataforma

import android.media.AudioManager
import android.media.ToneGenerator

private val tono by lazy { ToneGenerator(AudioManager.STREAM_MUSIC, 65) }

fun pitido() {
    tono.startTone(ToneGenerator.TONE_DTMF_1, 35)
}