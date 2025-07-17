package com.maze.mobile.domain.api

import android.graphics.Bitmap
import com.maze.mobile.domain.model.MazeList

interface MazeRepositoryApi {
    suspend fun fetchMazes(): MazeList
    suspend fun fetchMazeBitmap(encodedUrl: String): Bitmap
}