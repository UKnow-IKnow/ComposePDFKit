package com.example.composepdfkit.common.exception

sealed class PdfKitException(message: String, cause: Throwable? = null) :
    Exception(message, cause) {

    class FileAccessError(message: String, cause: Throwable? = null) :
        PdfKitException(message, cause)

    class GenerationError(message: String, cause: Throwable? = null) :
        PdfKitException(message, cause)

    class ViewerError(message: String, cause: Throwable? = null) :
        PdfKitException(message, cause)
}