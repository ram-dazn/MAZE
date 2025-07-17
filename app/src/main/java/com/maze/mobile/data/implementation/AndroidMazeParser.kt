package com.maze.mobile.data.implementation

import android.graphics.Bitmap
import android.graphics.Color
import com.maze.mobile.domain.api.MazeParserApi
import com.maze.mobile.domain.model.CellType
import com.maze.mobile.domain.model.MazeData
import javax.inject.Inject
import androidx.core.graphics.get
import com.maze.mobile.domain.model.Point
import androidx.core.graphics.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class AndroidMazeParser @Inject constructor() : MazeParserApi {

    override suspend fun parse(bitmap: Bitmap): MazeData = withContext(Dispatchers.Default) {
        val width = bitmap.width
        val height = bitmap.height
        val numThreads = 4
        val bandHeight = height / numThreads

        val deferred = (0 until numThreads).map { i ->
            val startY = i * bandHeight
            val endY = if (i == numThreads - 1) height else (i + 1) * bandHeight

            async {
                val localGrid = Array(endY - startY) { Array(width) { CellType.EMPTY } }
                var localStart: Point? = null
                var localEnd: Point? = null

                for (y in startY until endY) {
                    for (x in 0 until width) {
                        val pixel = bitmap[x, y]
                        val type = when(pixel) {
                            Color.BLACK -> CellType.WALL
                            Color.RED -> {
                                localStart = Point(x, y)
                                CellType.START
                            }
                            Color.BLUE -> {
                                localEnd = Point(x, y)
                                CellType.END
                            }
                            else -> CellType.EMPTY
                        }
                        localGrid[y - startY][x] = type
                    }
                }

                Triple(localGrid, localStart, localEnd)
            }
        }

        // Combine results
        val resultParts = deferred.awaitAll()

        val fullGrid = resultParts.flatMap { it.first.asList() }.toTypedArray()
        val start = resultParts.firstNotNullOfOrNull { it.second }
            ?: throw IllegalStateException("Start point not found")
        val end = resultParts.firstNotNullOfOrNull { it.third }
            ?: throw IllegalStateException("End point not found")

        MazeData(fullGrid, start, end)
    }

    override fun solveMaze(mazeData: MazeData): List<Point>? {
        val (grid, start, end) = mazeData
        val height = grid.size
        val width = grid[0].size

        val directions = listOf(
            0 to -1,  // Up
            0 to 1,   // Down
            -1 to 0,  // Left
            1 to 0    // Right
        )

        val visited = Array(height) { BooleanArray(width) }
        val cameFrom = Array(height) { Array<Point?>(width) { null } }

        val queue = ArrayDeque<Point>(height * width / 2)
        queue.add(start)
        visited[start.y][start.x] = true

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            if (current == end) break

            for ((dx, dy) in directions) {
                val nextX = current.x + dx
                val nextY = current.y + dy

                if (
                    nextX in 0 until width &&
                    nextY in 0 until height &&
                    !visited[nextY][nextX] &&
                    grid[nextY][nextX] != CellType.WALL
                ) {
                    visited[nextY][nextX] = true
                    cameFrom[nextY][nextX] = current
                    queue.add(Point(nextX, nextY))
                }
            }
        }

        if (!visited[end.y][end.x]) return null

        val path = mutableListOf<Point>()
        var current: Point? = end
        while (current != null) {
            path.add(current)
            current = cameFrom[current.y][current.x]
        }
        return path.reversed()
    }

    override fun drawPath(bitmap: Bitmap, path: List<Point>): Bitmap {
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        path.forEach { point ->
            if (point.x in 0 until mutableBitmap.width && point.y in 0 until mutableBitmap.height) {
                mutableBitmap[point.x, point.y] = Color.GREEN
            }
        }

        return mutableBitmap
    }
}