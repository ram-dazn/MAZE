package com.maze.mobile.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MazeDto(
    val name: String,
    val description: String,
    val url: String,
)
