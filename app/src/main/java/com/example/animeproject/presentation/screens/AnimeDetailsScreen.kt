package com.example.animeproject.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.animeproject.presentation.viewmodel.AnimeDetailsViewModel
import com.example.animeproject.presentation.viewmodel.AnimeViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailsScreen(
    onBackClick: () -> Unit,
    factory: AnimeViewModelFactory,
    viewModel: AnimeDetailsViewModel = viewModel(factory = factory)
) {
    val anime by viewModel.anime.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(anime?.title ?: "Детали") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                anime == null -> Text("Аниме не найдено", modifier = Modifier.align(Alignment.Center))
                else -> {
                    val a = anime!!
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = a.posterUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(300.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
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
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleLarge)
            Text(label, style = MaterialTheme.typography.bodySmall)
        }
    }
}