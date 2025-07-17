package com.maze.mobile.data.mapper

import com.maze.mobile.data.model.MazeDto
import com.maze.mobile.data.model.MazeListDto
import com.maze.mobile.domain.model.Maze
import com.maze.mobile.domain.model.MazeList

fun MazeDto.toDomain() = Maze(
    name = name,
    description = description,
    imageUrl = url
)

fun MazeListDto.toDomain() = MazeList(
    count = count,
    list = list.map { it.toDomain() }
)