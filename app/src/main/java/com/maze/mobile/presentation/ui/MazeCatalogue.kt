package com.maze.mobile.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.maze.mobile.domain.model.Maze
import com.maze.mobile.domain.model.MazeList
import com.maze.mobile.presentation.viewmodel.MazeListViewModel
import com.maze.mobile.presentation.viewmodel.MazeListViewModel.UiState.Loading
import com.maze.mobile.presentation.viewmodel.MazeListViewModel.UiState.Success
import com.maze.mobile.presentation.viewmodel.MazeListViewModel.UiState.Error

@Composable
fun MazeCatalogue(
    viewModel: MazeListViewModel = hiltViewModel(),
    onClick: (maze: Maze) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is Loading -> {
            CircularLoadingIndicator()
        }
        is Success -> {
            val success = (uiState as Success)
            Catalogue(success.mazeList, onClick)
        }
        is Error -> {
            Text(
                text = "No maze available right now.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun Catalogue(
    mazeList: MazeList,
    onClick: (maze: Maze) -> Unit,
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = mazeList.list,
            key = { it.imageUrl }
        ) { maze ->
            MazeProblem(maze, onClick)
        }
    }
}

@Composable
fun MazeProblem(
    maze: Maze,
    onClick: (maze: Maze) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clickable {
                onClick(maze)
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            AsyncImage(
                model = maze.imageUrl,
                contentDescription = "Maze Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(6.dp)
            ) {
                Text(
                    text = maze.name,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = maze.description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
@Preview
fun Preview() {
    val maze = Maze(
        name = "Maze",
        description = "Maze Description",
        imageUrl = "https://maze.image.png"
    )
    val mazeList = MazeList(
        count = 2,
        list = listOf(
            maze,
            maze.copy(imageUrl = "https://maze.image1.png")
        )
    )
    Catalogue(mazeList) {}
}