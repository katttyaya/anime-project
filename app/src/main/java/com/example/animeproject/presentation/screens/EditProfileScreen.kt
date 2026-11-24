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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeproject.presentation.viewmodel.EditProfileViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel,
    onDone: () -> Unit,
    onCancel: () -> Unit
) {
    val fullName by viewModel.fullName.collectAsState()
    val position by viewModel.position.collectAsState()
    val avatarUri by viewModel.avatarUri.collectAsState()
    val resumeUrl by viewModel.resumeUrl.collectAsState()
    val context = LocalContext.current

    var showSourceDialog by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.onAvatarUriChanged(it.toString()) }
    }

    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val tempUri = saveBitmapToCache(context, bitmap)
            tempUri?.let { viewModel.onAvatarUriChanged(it.toString()) }
        }
    }

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
                    IconButton(onClick = { viewModel.saveProfile(onDone) }) {
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
                        .clickable { showSourceDialog = true }
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
                        onClick = { viewModel.saveProfile(onDone) },
                        modifier = Modifier.weight(1f)
                    ) { Text("Готово") }
                }
            }
        }
    }

    if (showSourceDialog) {
        AlertDialog(
            onDismissRequest = { showSourceDialog = false },
            title = { Text("Откуда взять аватар?") },
            text = { Text("Выберите источник фото") },
            confirmButton = {
                TextButton(onClick = {
                    showSourceDialog = false
                    pickImageLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }) { Text("Галерея") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showSourceDialog = false
                    takePhotoLauncher.launch(null)
                }) { Text("Камера") }
            }
        )
    }
}

private fun saveBitmapToCache(
    context: android.content.Context,
    bitmap: android.graphics.Bitmap
): Uri? {
    return try {
        val file = File(context.cacheDir, "avatar_temp.jpg")
        file.outputStream().use { out ->
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, out)
        }
        Uri.fromFile(file)
    } catch (e: Exception) {
        null
    }
}