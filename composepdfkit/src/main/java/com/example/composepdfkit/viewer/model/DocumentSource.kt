package com.example.composepdfkit.viewer.model

sealed class DocumentSource {
    data class Url(val url: String) : DocumentSource()
    data class Uri(val uri: android.net.Uri) : DocumentSource()
}