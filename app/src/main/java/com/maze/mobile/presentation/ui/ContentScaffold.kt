package com.maze.mobile.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.maze.mobile.presentation.Screen
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScaffold(title: CharSequence) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val isMazeScreen = currentRoute?.startsWith(Screen.Mazes.route) == true
    val mazeName = currentBackStackEntry?.arguments?.getString("mazeName")

    val toolbarTitle = if (!isMazeScreen && !mazeName.isNullOrBlank()) mazeName else title.toString()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    if (!isMazeScreen) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                },
                title = {
                    Text(toolbarTitle)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Mazes.route
            ) {
                composable(Screen.Mazes.route) {
                    MazeCatalogue { maze ->
                        val encodedUrl = URLEncoder.encode(maze.imageUrl, "UTF-8")
                        val route = Screen.Solution.createRoute(encodedUrl, maze.name)
                        navController.navigate(route)
                    }
                }

                composable(
                    route = Screen.Solution.route,
                    arguments = listOf(
                        navArgument("url") { type = NavType.StringType },
                        navArgument("mazeName") { type = NavType.StringType }
                    )
                ) { nav ->
                    val url = nav.arguments?.getString("url").orEmpty()
                    SolvedMazeScreen(mazeEncodedUrl = url)
                }
            }
        }
    }
}