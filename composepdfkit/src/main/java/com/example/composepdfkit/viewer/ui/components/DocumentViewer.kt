package com.example.composepdfkit.viewer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composepdfkit.viewer.model.DocumentSource
import com.example.composepdfkit.viewer.model.ViewerState
import com.example.composepdfkit.viewer.ui.viewmodel.ViewerViewModel

@Composable
internal fun DocumentViewer(
    viewModel: ViewerViewModel,
    source: DocumentSource,
    isImage: Boolean,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(source) {
        viewModel.loadDocument(source, isImage)
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (val currentState = state) {
            ViewerState.Idle -> {
                // Initially empty
            }
            ViewerState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is ViewerState.Success -> {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(currentState.document.pageCount) { pageIndex ->
                        DocumentPageView(
                            document = currentState.document,
                            pageIndex = pageIndex,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
            is ViewerState.Error -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentState.message,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}