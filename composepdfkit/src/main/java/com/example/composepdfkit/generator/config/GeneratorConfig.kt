package com.example.composepdfkit.generator.config

import androidx.annotation.Keep
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.example.composepdfkit.common.config.PageConfig

@Keep
@Stable
data class GeneratorConfig(
    @Stable val name: String,
    @Stable val pageConfig: PageConfig = PageConfig(),
    @Stable val header: (@Composable () -> Unit)? = null,
    @Stable val footer: (@Composable () -> Unit)? = null,
    @Stable val body: @Composable () -> Unit,
    @Stable val metadata: DocumentMetadata = DocumentMetadata()
)

@Keep
@Stable
data class DocumentMetadata(
    val title: String = "",
    val author: String = "",
    val subject: String = "",
    val keywords: List<String> = emptyList(),
    val creator: String = "ComposePDFKit"
)