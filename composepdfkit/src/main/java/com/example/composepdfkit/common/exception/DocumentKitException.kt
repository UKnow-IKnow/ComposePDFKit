package com.example.composepdfkit.common.exception

sealed class DocumentKitException(message: String, cause: Throwable? = null) :
    Exception(message, cause) {

    class FileAccessError(message: String, cause: Throwable? = null) :
        DocumentKitException(message, cause)

    class GenerationError(message: String, cause: Throwable? = null) :
        DocumentKitException(message, cause)

    class ViewerError(message: String, cause: Throwable? = null) :
        DocumentKitException(message, cause)
}