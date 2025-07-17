package com.maze.mobile.presentation

import android.graphics.Bitmap
import app.cash.turbine.test
import com.maze.mobile.StandardTestDispatcherRule
import com.maze.mobile.domain.usecase.GetBitmapUseCase
import com.maze.mobile.domain.usecase.GetSolvedBitmapUseCase
import com.maze.mobile.presentation.viewmodel.SolveMazeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SolveMazeViewModelTest {

    @get:Rule
    val dispatcherRule = StandardTestDispatcherRule()

    private val getBitmap: GetBitmapUseCase = mockk()
    private val getSolvedBitmap: GetSolvedBitmapUseCase = mockk()
    private lateinit var viewModel: SolveMazeViewModel

    private val fakeBitmap = mockk<Bitmap>()

    @Before
    fun setup() {
        viewModel = SolveMazeViewModel(getBitmap, getSolvedBitmap)
    }

    @Test
    fun `solveMaze emits Success when bitmap is solved`() = runTest {
        coEvery { getBitmap(any()) } returns fakeBitmap
        coEvery { getSolvedBitmap(fakeBitmap) } returns fakeBitmap

        viewModel.uiState.test {
            assertEquals(SolveMazeViewModel.UiState.Loading, awaitItem())
            viewModel.solveMaze("https://example.com/image.png")
            advanceUntilIdle()
            val success = awaitItem()
            assertTrue(success is SolveMazeViewModel.UiState.Success)
            assertEquals(fakeBitmap, (success as SolveMazeViewModel.UiState.Success).solvedBitmap)
        }
    }

    @Test
    fun `solveMaze emits Error when exception occurs`() = runTest {
        coEvery { getBitmap(any()) } throws RuntimeException("Failed")

        viewModel.uiState.test {
            assertEquals(SolveMazeViewModel.UiState.Loading, awaitItem())
            viewModel.solveMaze("https://example.com/image.png")
            advanceUntilIdle()
            assertEquals(SolveMazeViewModel.UiState.Error, awaitItem())
        }
    }
}