package com.maze.mobile.data.api

import com.maze.mobile.data.model.MazeListDto
import retrofit2.http.GET

interface MazeApi {
    @GET("homework/mazes")
    suspend fun getMazes(): MazeListDto
}