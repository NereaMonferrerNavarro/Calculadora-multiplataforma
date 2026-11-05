package com.example.calculadoramultiplataforma

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

private var clipPitido: Clip? = null

@Synchronized
actual fun pitido() {
    try {
        val clip = clipPitido ?: run {
            val rate = 44100f
            val formato = AudioFormat(rate, 8, 1, true, false)
            val n = (rate * 0.03).toInt()

            val datos = ByteArray(n) { i ->
                val caida = 1.0 - i.toDouble() / n
                (Math.sin(2.0 * Math.PI * 600.0 * i / rate) *
                        50 * caida).toInt().toByte()
            }

            AudioSystem.getClip().apply {
                open(formato, datos, 0, datos.size)
            }.also {
                clipPitido = it
            }
        }

        clip.stop()
        clip.setFramePosition(0)
        clip.start()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}