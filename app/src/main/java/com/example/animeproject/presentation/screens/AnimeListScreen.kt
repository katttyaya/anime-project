package com.example.animeproject.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.animeproject.domain.model.Anime
import com.example.animeproject.presentation.viewmodel.AnimeListViewModel
import com.example.animeproject.presentation.viewmodel.AnimeViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeListScreen(
    onAnimeClick: (String) -> Unit,
    factory: AnimeViewModelFactory,
    viewModel: AnimeListViewModel = viewModel(factory = factory)
) {
    val animeList by viewModel.animeList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Список Аниме") })
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Ошибка: $error", color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadAnimeList("cowboy") }) {
                            Text("Попробовать снова")
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(animeList) { anime ->
                            AnimeListItem(anime = anime, onClick = { onAnimeClick(anime.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimeListItem(anime: Anime, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = anime.posterUrl,
                contentDescription = anime.title,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = anime.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Рейтинг: ${anime.rating}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Эпизоды: ${anime.episodes}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}