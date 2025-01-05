package com.example.composepdfkit.viewer

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.composepdfkit.viewer.core.DocumentReader
import com.example.composepdfkit.viewer.core.PdfReader
import com.example.composepdfkit.viewer.model.DocumentSource
import com.example.composepdfkit.viewer.ui.components.DocumentViewer
import com.example.composepdfkit.viewer.ui.viewmodel.ViewerViewModel

class ComposeDocumentViewer internal constructor(context: Context) {
    private val documentReader: DocumentReader = PdfReader(context)
    private val viewModel = ViewerViewModel(documentReader)

    @Composable
    fun View(
        source: DocumentSource,
        isImage: Boolean = false,
        modifier: Modifier = Modifier
    ) {
        DocumentViewer(
            viewModel = viewModel,
            source = source,
            isImage = isImage,
            modifier = modifier
        )
    }
}