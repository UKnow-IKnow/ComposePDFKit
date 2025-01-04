package com.example.composepdfkit.generator.model

import androidx.compose.ui.platform.ComposeView
import com.example.composepdfkit.common.config.PageConfig
import com.example.composepdfkit.generator.config.DocumentMetadata

internal data class DocumentConfig(
    val name: String,
    val pageConfig: PageConfig,
    val metadata: DocumentMetadata,
    val headerView: ComposeView?,
    val bodyView: ComposeView,
    val footerView: ComposeView?
)