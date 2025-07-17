package com.maze.mobile.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.maze.mobile.StandardTestDispatcherRule
import com.maze.mobile.data.api.MazeApi
import com.maze.mobile.data.api.NetworkConnectionApi
import com.maze.mobile.data.implementation.MazeRepository
import com.maze.mobile.data.model.MazeDto
import com.maze.mobile.data.model.MazeListDto
import com.maze.mobile.domain.model.Maze
import com.maze.mobile.domain.model.MazeList
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import java.io.ByteArrayInputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MazeRepositoryTest {

    @get:Rule
    val dispatcherRule = StandardTestDispatcherRule()

    private val mazeApi: MazeApi = mockk()
    private val mockNetworkConnection = mockk<NetworkConnectionApi>()
    private lateinit var repository: MazeRepository

    private val fakeDto = MazeListDto(
        count = 1,
        list = listOf(
            MazeDto("Maze1", "Maze1 Desc", "https://maze1.png")
        )
    )
    private val expectedDomain = MazeList(
        count = 1,
        list = listOf(
            Maze("Maze1", "Maze1 Desc", "https://maze1.png")
        )
    )

    @Before
    fun setUp() {
        repository = MazeRepository(mazeApi, mockNetworkConnection)
    }

    @Test
    fun `fetchMazes should return mapped domain model from API`() = runTest {
        coEvery { mazeApi.getMazes() } returns fakeDto

        val result = repository.fetchMazes()

        assertEquals(expectedDomain, result)
    }

    @Test
    fun `fetchMazeBitmap returns Bitmap on successful fetch`() = runTest {
        val encodedUrl = "http://example.com/maze.png"
        val url = URL(encodedUrl)
        val mockConnection = mockk<HttpURLConnection>(relaxed = true)
        val bitmapBytes = ByteArray(100)
        val inputStream = ByteArrayInputStream(bitmapBytes)
        val expectedBitmap = mockk<Bitmap>()

        mockkStatic(BitmapFactory::class)

        every { mockConnection.inputStream } returns inputStream
        every { BitmapFactory.decodeStream(inputStream) } returns expectedBitmap
        coEvery { mockNetworkConnection.openConnection(url) } returns mockConnection

        val result = repository.fetchMazeBitmap(encodedUrl)

        assertEquals(expectedBitmap, result)
        verify { mockConnection.connect() }
    }

    @Test
    fun `fetchMazeBitmap handles connection error`() = runTest {
        val encodedUrl = "http://example.com/maze.png"
        val url = URL(encodedUrl)
        val mockConnection = mockk<HttpURLConnection>(relaxed = true)

        coEvery { mockNetworkConnection.openConnection(url) } returns mockConnection
        every { mockConnection.responseCode } returns 404

        runCatching {
            repository.fetchMazeBitmap(encodedUrl)
        }.onSuccess {
            fail("Expected an exception but got success.")
        }.onFailure {
            assertTrue(it is Exception)
        }

        verify { mockConnection.connect() }
    }
}