package com.example.composepdfkit.generator

import android.content.Context
import com.example.composepdfkit.generator.config.GeneratorConfig
import com.example.composepdfkit.generator.core.DocumentGenerator
import com.example.composepdfkit.generator.core.PdfGenerator
import java.io.File

class ComposeDocumentGenerator internal constructor() {

    class ComposeDocumentGenerator internal constructor(
        context: Context
    ) {
        private val documentGenerator: DocumentGenerator = PdfGenerator(context)

        /**
         * Generates a document based on the provided configuration.
         * @param config Configuration for document generation
         * @return Generated document file
         */
        fun generate(config: GeneratorConfig): File {
            return documentGenerator.generate(config)
        }
    }
}