package com.maze.mobile.data.implementation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.maze.mobile.data.api.MazeApi
import com.maze.mobile.data.mapper.toDomain
import com.maze.mobile.domain.api.MazeRepositoryApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class MazeRepository @Inject constructor(
    private val mazeApi: MazeApi
): MazeRepositoryApi {

    override suspend fun fetchMazes() =
        mazeApi.getMazes().toDomain()

    override suspend fun fetchMazeBitmap(encodedUrl: String): Bitmap {
        val url = URL(encodedUrl)

        val connection = (withContext(Dispatchers.IO) {
            url.openConnection()
        } as HttpURLConnection).apply {
            connectTimeout = 10_000
            readTimeout = 10_000
            doInput = true
            requestMethod = "GET"
            connect()
        }

        connection.inputStream.use { input ->
            return BitmapFactory.decodeStream(input)
                ?: throw IllegalStateException("Failed to decode Bitmap from stream.")
        }
    }
}