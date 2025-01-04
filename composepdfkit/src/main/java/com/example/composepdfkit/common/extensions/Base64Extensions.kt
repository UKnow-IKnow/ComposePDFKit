package com.example.composepdfkit.common.extensions

import android.util.Base64
import java.io.File

/**
 * Extension functions for Base64 operations
 */

/**
 * Check if string is valid Base64
 */
fun String.isBase64(): Boolean {
    return try {
        Base64.decode(this, Base64.DEFAULT)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}

/**
 * Decode Base64 string to ByteArray
 * @throws IllegalArgumentException if string is not valid Base64
 */
fun String.decodeBase64ToByteArray(): ByteArray {
    require(isBase64()) { "Invalid Base64 string" }
    return Base64.decode(this, Base64.DEFAULT)
}

/**
 * Encode ByteArray to Base64 string
 */
fun ByteArray.encodeToBase64(): String {
    return Base64.encodeToString(this, Base64.NO_WRAP)
}

/**
 * Convert File to Base64 string
 */
fun File.toBase64(): String {
    return readBytes().encodeToBase64()
}

/**
 * Save Base64 string to file
 * @param file Target file
 * @throws IllegalArgumentException if string is not valid Base64
 */
fun String.saveBase64ToFile(file: File) {
    file.writeBytes(decodeBase64ToByteArray())
}