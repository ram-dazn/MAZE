package com.maze.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maze.mobile.domain.model.MazeList
import com.maze.mobile.domain.usecase.FetchMazesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MazeListViewModel @Inject constructor(
    private val fetchMazes: FetchMazesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadMazes()
    }

    private fun loadMazes() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { fetchMazes() }
                .onSuccess { mazes ->
                    _uiState.value = UiState.Success(mazes)
                }
                .onFailure {
                    // Optional: add error state
                }
        }
    }

    sealed interface UiState {
        data object Loading : UiState
        data class Success(val mazeList: MazeList) : UiState
    }
}