package com.example.animeproject.data

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeapp.ui.viewmodel.AnimeViewModel

@Composable
fun AnimeListScreen(
    viewModel: AnimeViewModel,
    onAnimeClick: (String) -> Unit // Колбэк для перехода на детали
) {
    val animeList by viewModel.animeList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Список Аниме") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(animeList) { anime ->
                AnimeListItem(anime = anime, onClick = { onAnimeClick(anime.id) })
            }
        }
    }
}

@Composable
fun AnimeListItem(anime: Anime, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            // Картинка
            AsyncImage(
                model = anime.posterUrl,
                contentDescription = anime.title,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Текст
            Column {
                Text(text = anime.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Рейтинг: ${anime.rating}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Эпизоды: ${anime.episodes}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}