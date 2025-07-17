package com.maze.mobile.domain.usecase

import com.maze.mobile.domain.api.MazeRepositoryApi
import com.maze.mobile.domain.model.MazeList
import javax.inject.Inject

class FetchMazesUseCase @Inject constructor(
    private val repository: MazeRepositoryApi
) {

    suspend operator fun invoke(): MazeList = repository.fetchMazes()
}