package com.example.composepdfkit.models.config


data class DocumentMargins(
    val top: Int = 40,
    val bottom: Int = 40,
    val left: Int = 40,
    val right: Int = 40
) {
    companion object {
        val Default = DocumentMargins()
        val None = DocumentMargins(0, 0, 0, 0)
    }

    fun toPixels(density: Float): DocumentMargins = DocumentMargins(
        top = (top * density).toInt(),
        bottom = (bottom * density).toInt(),
        left = (left * density).toInt(),
        right = (right * density).toInt()
    )
}
