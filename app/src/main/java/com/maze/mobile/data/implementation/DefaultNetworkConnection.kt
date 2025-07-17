package com.maze.mobile.data.implementation

import com.maze.mobile.data.api.NetworkConnectionApi
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class DefaultNetworkConnection @Inject constructor() : NetworkConnectionApi {

    override fun openConnection(url: URL): HttpURLConnection =
        url.openConnection() as HttpURLConnection
}