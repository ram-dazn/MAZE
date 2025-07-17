package com.maze.mobile.data.api

import java.net.HttpURLConnection
import java.net.URL

interface NetworkConnectionApi {
    fun openConnection(url: URL): HttpURLConnection
}