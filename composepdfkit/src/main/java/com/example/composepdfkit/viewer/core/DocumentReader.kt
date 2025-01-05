package com.example.composepdfkit.viewer.core

import com.example.composepdfkit.viewer.model.Document
import com.example.composepdfkit.viewer.model.DocumentSource

interface DocumentReader {
    suspend fun readFromSource(source: DocumentSource, isImage: Boolean = false): Document
}