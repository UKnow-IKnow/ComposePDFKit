package com.example.composepdfkit.models.document

data class DocumentMetadata(
    val title: String? = null,
    val author: String? = null,
    val subject: String? = null,
    val keywords: List<String> = emptyList(),
    val creator: String? = null,
    val producer: String? = null,
    val creationDate: Long = System.currentTimeMillis(),
    val modificationDate: Long = System.currentTimeMillis(),
    val encrypted: Boolean = false,
    val version: String? = null
) {
    companion object {
        val Empty = DocumentMetadata()
    }
}