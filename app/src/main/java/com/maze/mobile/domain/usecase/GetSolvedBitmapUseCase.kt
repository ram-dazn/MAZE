package com.maze.mobile.domain.usecase

import android.graphics.Bitmap
import com.maze.mobile.domain.api.MazeParserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSolvedBitmapUseCase @Inject constructor(
    private val mazeParserApi: MazeParserApi,
) {

    suspend operator fun invoke(bitmap: Bitmap): Bitmap = withContext(Dispatchers.Default) {
        val mazeData = mazeParserApi.parse(bitmap)
        val points = mazeParserApi.solveMaze(mazeData)
        return@withContext points?.let {
            mazeParserApi.drawPath(bitmap, points)
        } ?: throw IllegalStateException("No solution found.")
    }
}