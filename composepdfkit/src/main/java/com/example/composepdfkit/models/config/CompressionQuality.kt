package com.example.composepdfkit.models.config

enum class CompressionQuality(val value: Int) {
    LOW(30),
    MEDIUM(60),
    HIGH(80),
    LOSSLESS(100);

    companion object {
        val default = MEDIUM
    }
}
