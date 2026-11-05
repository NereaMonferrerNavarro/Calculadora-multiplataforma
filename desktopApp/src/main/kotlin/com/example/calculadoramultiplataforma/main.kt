package com.example.calculadoramultiplataforma

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CalculadoraMultiplataforma",
    ) {
        App()
    }
}