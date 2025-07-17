package com.maze.mobile.presentation.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maze.mobile.presentation.viewmodel.SolveMazeViewModel

@Composable
fun SolvedMazeScreen(
    viewModel: SolveMazeViewModel = hiltViewModel(),
    mazeEncodedUrl: String,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(mazeEncodedUrl) {
        viewModel.solveMaze(mazeEncodedUrl)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (val state = uiState) {
            is SolveMazeViewModel.UiState.Loading -> {
                CircularLoadingIndicator()
            }

            is SolveMazeViewModel.UiState.Success -> {
                ZoomAbleImageView(state.solvedBitmap)
            }

            is SolveMazeViewModel.UiState.Error -> {
                Text(
                    text = "Failed to solve maze.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ZoomAbleImageView(solvedBitmap: Bitmap) {
    val minScale = 1f
    val maxScale = 5f

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var rotation by remember { mutableFloatStateOf(0f) }

    val transformableState = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale = (scale * zoomChange).coerceIn(minScale, maxScale)
        offset += offsetChange
        rotation += rotationChange
    }

    val doubleTapReset = Modifier.pointerInput(Unit) {
        detectTapGestures(
            onDoubleTap = {
                scale = 1f
                offset = Offset.Zero
                rotation = 0f
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(doubleTapReset)
            .transformable(transformableState)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x.coerceIn(-500f, 500f),
                translationY = offset.y.coerceIn(-500f, 500f),
                rotationZ = rotation
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = solvedBitmap.asImageBitmap(),
            contentDescription = "Solved maze image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}