package com.example.composepdfkit.generator.core

import com.example.composepdfkit.generator.config.GeneratorConfig
import java.io.File

interface DocumentGenerator {
    /**
     * Generates a document based on the provided configuration
     * @param config Configuration for document generation
     * @return Generated document file
     */
    fun generate(config: GeneratorConfig): File
}