package com.example.composepdfkit.models.document

import android.net.Uri
import java.io.File

sealed class DocumentSource {
    data class FromUrl(val url: String) : DocumentSource()
    data class FromUri(val uri: Uri) : DocumentSource()
    data class FromFile(val file: File) : DocumentSource()
    data class FromBytes(val bytes: ByteArray) : DocumentSource() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as FromBytes
            return bytes.contentEquals(other.bytes)
        }
        override fun hashCode(): Int = bytes.contentHashCode()
    }
    data class FromBase64(val base64String: String) : DocumentSource()

    companion object {
        fun from(url: String) = FromUrl(url)
        fun from(uri: Uri) = FromUri(uri)
        fun from(file: File) = FromFile(file)
        fun from(bytes: ByteArray) = FromBytes(bytes)
        fun fromBase64(base64: String) = FromBase64(base64)
    }
}