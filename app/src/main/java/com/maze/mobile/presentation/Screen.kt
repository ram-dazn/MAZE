package com.maze.mobile.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    data object Mazes : Screen("mazes_screen")
    data object Solution : Screen("solution_screen/{url}/{mazeName}") {
        fun createRoute(url: String, mazeName: String) = "solution_screen/$url/$mazeName"
    }
}