package com.example.composepdfkit.models.document

sealed class DocumentException(message: String, cause: Throwable? = null) :
    Exception(message, cause)

class DocumentLoadException(
    message: String = "Failed to load document",
    cause: Throwable? = null
) : DocumentException(message, cause)

class DocumentParseException(
    message: String = "Failed to parse document",
    cause: Throwable? = null
) : DocumentException(message, cause)

class DocumentGenerationException(
    message: String = "Failed to generate document",
    cause: Throwable? = null
) : DocumentException(message, cause)

class DocumentSaveException(
    message: String = "Failed to save document",
    cause: Throwable? = null
) : DocumentException(message, cause)