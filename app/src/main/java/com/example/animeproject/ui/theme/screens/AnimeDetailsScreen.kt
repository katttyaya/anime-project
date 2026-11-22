package com.example.animeproject.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.animeproject.ui.theme.viewmodel.AnimeDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailsScreen(
    onBackClick: () -> Unit,
    viewModel: AnimeDetailsViewModel = viewModel()
) {
    val anime by viewModel.anime.collectAsState()

    if (anime == null) {
        Box(modifier = Modifier.fillMaxSize()) { Text("Аниме не найдено") }
        return
    }

    val currentAnime = anime!!

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentAnime.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = currentAnime.posterResId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = currentAnime.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    InfoBox(label = "Рейтинг", value = currentAnime.rating)
                    InfoBox(label = "Эпизоды", value = currentAnime.episodes.toString())
                    InfoBox(label = "Статус", value = currentAnime.status)
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Описание", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = currentAnime.synopsis, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun InfoBox(label: String, value: String) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = value, style = MaterialTheme.typography.titleLarge)
            Text(text = label, style = MaterialTheme.typography.bodySmall)
        }
    }
}