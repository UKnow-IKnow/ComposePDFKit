package com.example.composepdfkit.models.config

import android.util.DisplayMetrics

enum class PageSize(
    val widthPoints: Int,
    val heightPoints: Int,
    val widthInches: Double,
    val heightInches: Double,
    val widthMm: Int,
    val heightMm: Int
) {
    A0(2384, 3370, 33.1, 46.8, 841, 1189),
    A1(1684, 2384, 23.4, 33.1, 594, 841),
    A2(1191, 1684, 16.5, 23.4, 420, 594),
    A3(842, 1191, 11.7, 16.5, 297, 420),
    A4(595, 842, 8.27, 11.7, 210, 297),
    A5(420, 595, 5.83, 8.27, 148, 210),
    A6(298, 420, 4.13, 5.83, 105, 148),
    LETTER(612, 792, 8.5, 11.0, 216, 279),
    LEGAL(612, 1008, 8.5, 14.0, 216, 356);

    fun toPixels(densityDpi: Int): Pair<Int, Int> {
        val widthPixels = (widthPoints * densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        val heightPixels = (heightPoints * densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        return Pair(widthPixels, heightPixels)
    }

    companion object {
        val default = A4
    }
}