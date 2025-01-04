package com.example.composepdfkit.models.config

enum class PageOrientation {
    PORTRAIT,
    LANDSCAPE;

    fun isPortrait(): Boolean = this == PORTRAIT
    fun isLandscape(): Boolean = this == LANDSCAPE

    companion object {
        val default = PORTRAIT
    }
}