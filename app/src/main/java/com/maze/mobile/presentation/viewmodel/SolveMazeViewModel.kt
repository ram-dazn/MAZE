package com.maze.mobile.presentation.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maze.mobile.domain.usecase.GetBitmapUseCase
import com.maze.mobile.domain.usecase.GetSolvedBitmapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SolveMazeViewModel @Inject constructor(
    private val getBitmap: GetBitmapUseCase,
    private val getSolvedBitmap: GetSolvedBitmapUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun solveMaze(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bitmap = getBitmap(url)
                val solvedBitmap = getSolvedBitmap(bitmap)
                _uiState.value = UiState.Success(solvedBitmap)
            } catch (e: Exception) {
                _uiState.value = UiState.Error
            }
        }
    }

    sealed interface UiState {
        data object Loading : UiState
        data class Success(val solvedBitmap: Bitmap) : UiState
        data object Error : UiState
    }
}