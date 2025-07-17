package com.maze.mobile.domain

import com.maze.mobile.domain.api.MazeRepositoryApi
import com.maze.mobile.domain.model.Maze
import com.maze.mobile.domain.model.MazeList
import com.maze.mobile.domain.usecase.FetchMazesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail

class FetchMazesUseCaseTest {

    private val repository: MazeRepositoryApi = mockk()
    private lateinit var useCase: FetchMazesUseCase
    private val fakeMazes = MazeList(
        count = 1,
        list = listOf(Maze("name", "desc", "url"))
    )

    @Before
    fun setup() {
        useCase = FetchMazesUseCase(repository)
    }

    @Test
    fun `should return maze list from repository`() = runTest {
        coEvery { repository.fetchMazes() } returns fakeMazes

        val result = useCase()
        assertEquals(fakeMazes, result)
    }

    @Test
    fun `should throw when not fetched from repository`() = runTest {
        coEvery { repository.fetchMazes() } throws Exception("Not Valid")

        runCatching { useCase() }
            .onSuccess {
                fail("Expected an exception but got success.")
            }
            .onFailure {
                assertTrue(it is Exception)
                assertEquals("Not Valid", it.message)
            }
    }
}