package com.example.composepdfkit.viewer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composepdfkit.viewer.core.DocumentReader
import com.example.composepdfkit.viewer.model.DocumentSource
import com.example.composepdfkit.viewer.model.ViewerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class ViewerViewModel(
    private val documentReader: DocumentReader
) : ViewModel() {
    private val _state = MutableStateFlow<ViewerState>(ViewerState.Idle)
    val state: StateFlow<ViewerState> = _state

    fun loadDocument(source: DocumentSource, isImage: Boolean = false) {
        _state.value = ViewerState.Loading
        viewModelScope.launch {
            try {
                val document = documentReader.readFromSource(source, isImage)
                _state.value = ViewerState.Success(document)
            } catch (e: Exception) {
                _state.value = ViewerState.Error(e.message ?: "Unknown error")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (_state.value is ViewerState.Success) {
            (_state.value as ViewerState.Success).document.close()
        }
    }
}
