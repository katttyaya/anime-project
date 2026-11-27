package com.example.animeproject.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeproject.presentation.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel, onAnimeClick: (String) -> Unit) {
    val favorites by viewModel.favorites.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Избранное") }) }) { padding ->
        if (favorites.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Нет избранных аниме.\nДобавляйте со списка или деталей.")
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favorites, key = { it.id }) { anime ->
                    Card(
                        Modifier.fillMaxWidth().clickable { onAnimeClick(anime.id) },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                anime.posterUrl, null,
                                Modifier.size(64.dp),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.width(16.dp))
                            Column(Modifier.weight(1f)) {
                                Text(anime.title, style = MaterialTheme.typography.titleMedium)
                                Text("Статус: ${anime.status}", style = MaterialTheme.typography.bodySmall)
                            }
                            IconButton(onClick = { viewModel.toggleFavorite(anime) }) {
                                Icon(Icons.Default.Favorite, "Удалить", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}