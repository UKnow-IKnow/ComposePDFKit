package com.example.composepdfkit.viewer.model

import android.graphics.Bitmap

data class DocumentPage(
    val width: Int,
    val height: Int,
    val render: suspend (Float) -> Bitmap
)