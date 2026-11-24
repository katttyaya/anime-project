package com.example.animeproject.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.animeproject.presentation.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel, onDone: () -> Unit) {
    val search by viewModel.searchQuery.collectAsState()
    val minRating by viewModel.minRating.collectAsState()
    val status by viewModel.statusFilter.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val statuses = listOf("Any", "finished", "current", "upcoming")

    Scaffold(topBar = { TopAppBar(title = { Text("Настройки фильтрации") }) }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OutlinedTextField(
                search, { viewModel.onSearchChanged(it) },
                label = { Text("Название / поиск") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            ExposedDropdownMenuBox(expanded, { expanded = !expanded }) {
                OutlinedTextField(
                    status, {}, readOnly = true,
                    label = { Text("Статус") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded, { expanded = false }) {
                    statuses.forEach { s ->
                        DropdownMenuItem(
                            text = { Text(s) },
                            onClick = { viewModel.onStatusChanged(s); expanded = false }
                        )
                    }
                }
            }

            Column {
                Text("Минимальный рейтинг: $minRating")
                Slider(minRating, { viewModel.onMinRatingChanged(it) }, valueRange = 0f..10f)
            }

            Spacer(Modifier.weight(1f))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    { viewModel.resetSettings(onDone) },
                    Modifier.weight(1f)
                ) { Text("Сбросить") }
                Button(
                    { viewModel.saveSettings(onDone) },
                    Modifier.weight(1f)
                ) { Text("Готово") }
            }
        }
    }
}