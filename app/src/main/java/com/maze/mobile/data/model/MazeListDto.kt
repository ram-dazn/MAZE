package com.maze.mobile.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MazeListDto(
    val count: Int,
    val list: List<MazeDto>,
)
