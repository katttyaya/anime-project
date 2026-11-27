package com.example.animeproject.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeproject.presentation.viewmodel.EditProfileViewModel
import com.example.animeproject.util.AlarmHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel,
    onDone: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val fullName by viewModel.fullName.collectAsState()
    val position by viewModel.position.collectAsState()
    val avatarUri by viewModel.avatarUri.collectAsState()
    val resumeUrl by viewModel.resumeUrl.collectAsState()
    val favoritePairTime by viewModel.favoritePairTime.collectAsState()
    val timeError by viewModel.timeError.collectAsState()
    val isSaveEnabled by viewModel.isSaveEnabled.collectAsState()

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.onAvatarUriChanged(it.toString()) }
    }

    var showTimePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактирование") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.saveProfile {
                                if (favoritePairTime.isNotBlank() && fullName.isNotBlank()) {
                                    AlarmHelper.setAlarm(context, favoritePairTime, fullName)
                                }
                                onDone()
                            }
                        },
                        enabled = isSaveEnabled
                    ) {
                        Icon(Icons.Default.Check, "Готово")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .size(120.dp)
                        .clickable {
                            pickImageLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                ) {
                    if (avatarUri.isNotBlank()) {
                        AsyncImage(
                            model = avatarUri,
                            contentDescription = "Аватар",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Нажмите на аватар для выбора фото",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = viewModel::onFullNameChanged,
                    label = { Text("Имя / ФИО") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = position,
                    onValueChange = viewModel::onPositionChanged,
                    label = { Text("Должность") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = resumeUrl,
                    onValueChange = viewModel::onResumeUrlChanged,
                    label = { Text("Ссылка на резюме (https://...)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = favoritePairTime,
                    onValueChange = viewModel::onFavoritePairTimeChanged,
                    label = { Text("Время любимой пары") },
                    placeholder = { Text("18:43") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = timeError != null,
                    trailingIcon = {
                        IconButton(onClick = { showTimePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "Выбрать время"
                            )
                        }
                    },
                    supportingText = {
                        if (timeError != null) {
                            Text(text = timeError!!)
                        }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f)
                    ) { Text("Отмена") }
                    Button(
                        onClick = {
                            viewModel.saveProfile {
                                if (favoritePairTime.isNotBlank() && fullName.isNotBlank()) {
                                    AlarmHelper.setAlarm(context, favoritePairTime, fullName)
                                }
                                onDone()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = isSaveEnabled
                    ) { Text("Готово") }
                }
            }
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            onDismiss = { showTimePicker = false },
            onConfirm = { hour, minute ->
                val timeString = String.format("%02d:%02d", hour, minute)
                viewModel.onFavoritePairTimeChanged(timeString)
                showTimePicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val currentTime = java.util.Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(java.util.Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(java.util.Calendar.MINUTE),
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите время") },
        text = {
            TimePicker(state = timePickerState)
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(timePickerState.hour, timePickerState.minute)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}