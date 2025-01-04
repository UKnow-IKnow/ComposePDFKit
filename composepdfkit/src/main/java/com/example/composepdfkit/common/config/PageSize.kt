package com.example.composepdfkit.common.config

import android.util.DisplayMetrics

enum class PageSize(
    val widthPoints: Int,
    val heightPoints: Int,
    val widthMm: Int,
    val heightMm: Int
) {
    A4(595, 842, 210, 297),
    A5(420, 595, 148, 210),
    LETTER(612, 792, 216, 279);

    fun toPixels(densityDpi: Int): Pair<Int, Int> {
        val widthPixels = (widthPoints * densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        val heightPixels = (heightPoints * densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        return Pair(widthPixels, heightPixels)
    }
}