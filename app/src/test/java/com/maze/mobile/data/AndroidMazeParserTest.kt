package com.maze.mobile.data

import android.graphics.Bitmap
import android.graphics.Color
import com.maze.mobile.data.implementation.AndroidMazeParser
import com.maze.mobile.domain.model.CellType
import com.maze.mobile.domain.model.MazeData
import com.maze.mobile.domain.model.Point
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull

class AndroidMazeParserTest {

    private lateinit var parser: AndroidMazeParser
    private val mockBitmap = mockk<Bitmap>(relaxed = true) {
        val pixelMap = mutableMapOf<Point, Int>()
        every { width } returns 5
        every { height } returns 5
        every { setPixel(any(), any(), any()) } answers {
            val x = firstArg<Int>()
            val y = secondArg<Int>()
            val color = thirdArg<Int>()
            pixelMap[Point(x, y)] = color
        }
        every { getPixel(any(), any()) } answers {
            val x = firstArg<Int>()
            val y = secondArg<Int>()
            pixelMap[Point(x, y)] ?: Color.TRANSPARENT
        }
    }

    @Before
    fun setup() {
        parser = AndroidMazeParser()
    }

    @Test
    fun `parse should correctly identify start end and walls`() = runTest {

        // Set a simple maze:
        // [W, W, W, W, W]
        // [W, R, _, B, W]
        // [W, W, W, W, W]

        mockBitmap.setPixel(0, 0, Color.BLACK)
        mockBitmap.setPixel(4, 0, Color.BLACK)
        mockBitmap.setPixel(0, 1, Color.BLACK)
        mockBitmap.setPixel(1, 1, Color.RED)   // Start
        mockBitmap.setPixel(3, 1, Color.BLUE)  // End
        mockBitmap.setPixel(4, 1, Color.BLACK)

        val result = parser.parse(mockBitmap)

        assertEquals(Point(1, 1), result.start)
        assertEquals(Point(3, 1), result.end)
        assertEquals(CellType.START, result.grid[1][1])
        assertEquals(CellType.END, result.grid[1][3])
        assertEquals(CellType.WALL, result.grid[1][0])
    }

    @Test
    fun `solveMaze should find valid path`() = runTest {
        val grid = arrayOf(
            arrayOf(CellType.WALL, CellType.WALL, CellType.WALL, CellType.WALL, CellType.WALL),
            arrayOf(CellType.WALL, CellType.START, CellType.EMPTY, CellType.END, CellType.WALL),
            arrayOf(CellType.WALL, CellType.WALL, CellType.WALL, CellType.WALL, CellType.WALL)
        )
        val data = MazeData(grid, start = Point(1, 1), end = Point(3, 1))

        val path = parser.solveMaze(data)

        assertNotNull(path)
        assertEquals(Point(1, 1), path?.first())
        assertEquals(Point(3, 1), path?.last())
    }

    @Test
    fun `drawPath should modify bitmap with green pixels`() {
        every { mockBitmap.copy(Bitmap.Config.ARGB_8888, true) } returns mockBitmap

        val path = listOf(Point(1, 1), Point(2, 1), Point(3, 1))

        val parser = AndroidMazeParser()
        val result = parser.drawPath(mockBitmap, path)

        assertEquals(Color.GREEN, result.getPixel(1, 1))
        assertEquals(Color.GREEN, result.getPixel(2, 1))
        assertEquals(Color.GREEN, result.getPixel(3, 1))
    }
}