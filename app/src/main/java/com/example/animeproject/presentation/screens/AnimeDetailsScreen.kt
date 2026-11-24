package com.example.animeproject.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeproject.presentation.viewmodel.AnimeDetailsViewModel
import com.example.animeproject.presentation.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailsScreen(viewModel: AnimeDetailsViewModel, onBackClick: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val isFav by viewModel.isFavorite.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text((uiState as? UiState.Success)?.data?.title ?: "Детали") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
                actions = {
                    val a = (uiState as? UiState.Success)?.data
                    if (a != null) {
                        IconButton(onClick = { viewModel.toggleFavorite(a) }) {
                            Icon(
                                if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                "Избранное",
                                tint = if (isFav) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (val s = uiState) {
                is UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is UiState.Error -> Text(s.message, Modifier.align(Alignment.Center))
                is UiState.Success -> {
                    val a = s.data
                    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                        AsyncImage(
                            a.posterUrl, null,
                            Modifier.fillMaxWidth().height(300.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(Modifier.padding(16.dp)) {
                            Text(a.title, style = MaterialTheme.typography.headlineMedium)
                            Spacer(Modifier.height(16.dp))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                InfoBox("Рейтинг", a.rating)
                                InfoBox("Эпизоды", a.episodes.toString())
                                InfoBox("Статус", a.status)
                            }
                            Spacer(Modifier.height(24.dp))
                            Text("Описание", style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(8.dp))
                            Text(a.synopsis, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoBox(label: String, value: String) {
    Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleLarge)
            Text(label, style = MaterialTheme.typography.bodySmall)
        }
    }
}