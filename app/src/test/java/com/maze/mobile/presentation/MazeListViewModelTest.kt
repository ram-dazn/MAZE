package com.maze.mobile.presentation

import com.maze.mobile.StandardTestDispatcherRule
import com.maze.mobile.domain.model.Maze
import com.maze.mobile.domain.model.MazeList
import com.maze.mobile.domain.usecase.FetchMazesUseCase
import com.maze.mobile.presentation.viewmodel.MazeListViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import app.cash.turbine.test

@ExperimentalCoroutinesApi
class MazeListViewModelTest {

    @get:Rule
    val dispatcherRule = StandardTestDispatcherRule()

    private val fakeMazes = MazeList(
        count = 1,
        list = listOf(Maze("name", "desc", "url"))
    )
    private val fetchMazesUseCase: FetchMazesUseCase = mockk()
    private lateinit var viewModel: MazeListViewModel

    @Before
    fun setup() {
        coEvery { fetchMazesUseCase() } returns fakeMazes
        viewModel = MazeListViewModel(fetchMazesUseCase)
    }

    @Test
    fun `should emit Success state with maze list`() = runTest {
        viewModel.uiState.test {
            advanceUntilIdle()
            assertEquals(MazeListViewModel.UiState.Success(fakeMazes), awaitItem())
        }
    }
}