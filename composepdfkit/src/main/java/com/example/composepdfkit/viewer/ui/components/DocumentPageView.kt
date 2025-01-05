package com.example.composepdfkit.viewer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.example.composepdfkit.viewer.model.Document
import com.example.composepdfkit.viewer.model.DocumentPage

@Composable
internal fun DocumentPageView(
    document: Document,
    pageIndex: Int,
    modifier: Modifier = Modifier
) {
    var page by remember { mutableStateOf<DocumentPage?>(null) }
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    LaunchedEffect(document, pageIndex) {
        page = document.getPage(pageIndex)
    }

    LaunchedEffect(page) {
        bitmap = page?.render?.let { it(1f) }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(
                page?.let { it.width.toFloat() / it.height.toFloat() } ?: 1f
            )
    ) {
        bitmap?.let { renderedBitmap ->
            Image(
                bitmap = renderedBitmap.asImageBitmap(),
                contentDescription = "Page ${pageIndex + 1}",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        } ?: CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}