package com.maze.mobile.domain.api

import android.graphics.Bitmap
import com.maze.mobile.domain.model.MazeData
import com.maze.mobile.domain.model.Point

interface MazeParserApi {
    suspend fun parse(bitmap: Bitmap): MazeData
    fun solveMaze(mazeData: MazeData): List<Point>?
    fun drawPath(bitmap: Bitmap, path: List<Point>): Bitmap
}