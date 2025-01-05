package com.example.composepdfkit.viewer.model

data class Document(
    val pageCount: Int,
    val getPage: suspend (Int) -> DocumentPage,
    val close: () -> Unit
)