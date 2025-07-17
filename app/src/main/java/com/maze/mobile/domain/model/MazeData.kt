package com.maze.mobile.domain.model

data class MazeData(
    val grid: Array<Array<CellType>>,
    val start: Point,
    val end: Point,
)

enum class CellType {
    WALL,
    EMPTY,
    START,
    END,
}

data class Point(
    val x: Int,
    val y: Int,
)