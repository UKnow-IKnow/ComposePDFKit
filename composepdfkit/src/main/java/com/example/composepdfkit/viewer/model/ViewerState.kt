package com.example.composepdfkit.viewer.model

sealed class ViewerState {
    object Idle : ViewerState()
    object Loading : ViewerState()
    data class Success(val document: Document) : ViewerState()
    data class Error(val message: String) : ViewerState()
}