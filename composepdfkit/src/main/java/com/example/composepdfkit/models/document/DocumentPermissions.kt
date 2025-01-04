package com.example.composepdfkit.models.document

data class DocumentPermissions(
    val canPrint: Boolean = true,
    val canModify: Boolean = true,
    val canCopy: Boolean = true,
    val canAnnotate: Boolean = true,
    val canFillForms: Boolean = true,
    val canExtractPages: Boolean = true,
    val canAssemble: Boolean = true,
    val canPrintHighQuality: Boolean = true
) {
    companion object {
        val FullPermissions = DocumentPermissions()
        val ReadOnly = DocumentPermissions(
            canPrint = true,
            canModify = false,
            canCopy = true,
            canAnnotate = false,
            canFillForms = false,
            canExtractPages = false,
            canAssemble = false,
            canPrintHighQuality = true
        )
    }
}