package com.example.calculadoramultiplataforma

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform