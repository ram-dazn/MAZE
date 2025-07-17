package com.maze.mobile.domain.usecase

import com.maze.mobile.domain.api.MazeRepositoryApi
import javax.inject.Inject

class GetBitmapUseCase @Inject constructor(
    private val repository: MazeRepositoryApi,
) {

    suspend operator fun invoke(encodedUrl: String) =
        repository.fetchMazeBitmap(encodedUrl)
}