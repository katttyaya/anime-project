package com.example.animeproject.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeproject.domain.model.Anime
import com.example.animeproject.presentation.viewmodel.AnimeListViewModel
import com.example.animeproject.presentation.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AnimeListScreen(viewModel: AnimeListViewModel, onAnimeClick: (String) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    var dialogAnime by remember { mutableStateOf<Anime?>(null) }

    if (dialogAnime != null) {
        val a = dialogAnime!!
        val isFav = favorites.any { it.id == a.id }
        AlertDialog(
            onDismissRequest = { dialogAnime = null },
            title = { Text(a.title) },
            text = { Text(if (isFav) "Удалить из избранного?" else "Добавить в избранное (доступно без интернета)?") },
            confirmButton = {
                TextButton(onClick = { viewModel.toggleFavorite(a); dialogAnime = null }) {
                    Text(if (isFav) "Удалить" else "Добавить")
                }
            },
            dismissButton = {
                TextButton(onClick = { dialogAnime = null }) { Text("Отмена") }
            }
        )
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Список Аниме") }) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (val s = uiState) {
                is UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is UiState.Error -> Column(
                    Modifier.align(Alignment.Center).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(s.message, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { /* перезагрузка через Settings */ }) { Text("Повторить") }
                }
                is UiState.Success -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(s.data, key = { it.id }) { anime ->
                        val isFav = favorites.any { it.id == anime.id }
                        AnimeListItem(
                            anime, isFav,
                            onClick = { onAnimeClick(anime.id) },
                            onLongClick = { dialogAnime = anime },
                            onFavoriteClick = { viewModel.toggleFavorite(anime) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimeListItem(
    anime: Anime, isFavorite: Boolean,
    onClick: () -> Unit, onLongClick: () -> Unit, onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                anime.posterUrl, null,
                Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(anime.title, style = MaterialTheme.typography.titleMedium)
                Text("Рейтинг: ${anime.rating}", style = MaterialTheme.typography.bodySmall)
                Text("Эпизоды: ${anime.episodes}", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    "Избранное",
                    tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}