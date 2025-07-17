package com.maze.mobile.domain

import android.graphics.Bitmap
import com.maze.mobile.StandardTestDispatcherRule
import com.maze.mobile.domain.api.MazeParserApi
import com.maze.mobile.domain.model.CellType
import com.maze.mobile.domain.model.MazeData
import com.maze.mobile.domain.model.Point
import com.maze.mobile.domain.usecase.GetSolvedBitmapUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class GetSolvedBitmapUseCaseTest {

    @get:Rule
    val dispatcherRule = StandardTestDispatcherRule()

    private val mazeParserApi: MazeParserApi = mockk()
    private lateinit var useCase: GetSolvedBitmapUseCase

    private val fakeBitmap = mockk<Bitmap>()
    private val fakeSolvedBitmap = mockk<Bitmap>()
    private val fakeMazeData = MazeData(
        grid = Array(10) { Array(10) { CellType.EMPTY } },
        start = Point(0, 0),
        end = Point(9, 9)
    )
    private val path = listOf(Point(0, 0), Point(1, 0), Point(2, 0))

    @Before
    fun setUp() {
        useCase = GetSolvedBitmapUseCase(mazeParserApi)
    }

    @Test
    fun `returns solved bitmap when path is found`() = runTest {
        coEvery { mazeParserApi.parse(fakeBitmap) } returns fakeMazeData
        coEvery { mazeParserApi.solveMaze(fakeMazeData) } returns path
        coEvery { mazeParserApi.drawPath(fakeBitmap, path) } returns fakeSolvedBitmap

        val result = useCase(fakeBitmap)

        assertEquals(fakeSolvedBitmap, result)
    }

    @Test
    fun `throws when no solution found`() = runTest {
        coEvery { mazeParserApi.parse(fakeBitmap) } returns fakeMazeData
        coEvery { mazeParserApi.solveMaze(fakeMazeData) } returns null

        val exception = assertFailsWith<IllegalStateException> {
            useCase(fakeBitmap)
        }

        assertEquals("No solution found.", exception.message)
    }
}