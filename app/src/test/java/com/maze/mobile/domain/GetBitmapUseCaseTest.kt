package com.maze.mobile.domain

import android.graphics.Bitmap
import com.maze.mobile.domain.api.MazeRepositoryApi
import com.maze.mobile.domain.usecase.GetBitmapUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail

class GetBitmapUseCaseTest {

    private val repository: MazeRepositoryApi = mockk()
    private lateinit var useCase: GetBitmapUseCase
    private val fakeBitmap = mockk<Bitmap>()
    private val url = "https://example.com/image.png"

    @Before
    fun setup() {
        useCase = GetBitmapUseCase(repository)
    }

    @Test
    fun `should return bitmap list from repository`() = runTest {
        coEvery { repository.fetchMazeBitmap(url) } returns fakeBitmap

        val result = useCase(url)
        assertEquals(fakeBitmap, result)
    }

    @Test
    fun `should throw when not bitmap not fetched from url`() = runTest {
        coEvery { repository.fetchMazeBitmap(url) } throws Exception("Not Valid")

        runCatching { useCase(url) }
            .onSuccess {
                fail("Expected an exception but got success.")
            }
            .onFailure {
                assertTrue(it is Exception)
                assertEquals("Not Valid", it.message)
            }
    }
}