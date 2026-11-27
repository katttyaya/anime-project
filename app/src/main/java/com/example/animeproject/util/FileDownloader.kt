package com.example.animeproject.util

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

object FileDownloader {

    private fun normalizeUrl(url: String): String {
        Regex("docs\\.google\\.com/document/d/([a-zA-Z0-9_-]+)")
            .find(url)?.let { m ->
                return "https://docs.google.com/document/d/${m.groupValues[1]}/export?format=pdf"
            }

        Regex("drive\\.google\\.com/file/d/([a-zA-Z0-9_-]+)")
            .find(url)?.let { m ->
                return "https://drive.google.com/uc?export=download&id=${m.groupValues[1]}"
            }

        return url
    }

    suspend fun downloadToCache(context: Context, rawUrl: String): File? {
        if (rawUrl.isBlank()) {
            showToast(context, "Ссылка на файл не указана")
            return null
        }

        val url = normalizeUrl(rawUrl)

        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connectTimeout = 15000
                connection.readTimeout = 15000
                connection.instanceFollowRedirects = true
                connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 Chrome/120 Mobile Safari/537.36"
                )

                val code = connection.responseCode
                if (code != HttpURLConnection.HTTP_OK) {
                    showToast(context, "Сервер вернул ошибку $code")
                    return@withContext null
                }

                val inputStream = connection.inputStream
                val fileName = "resume_${System.currentTimeMillis()}.pdf"
                val file = File(context.cacheDir, fileName)

                file.outputStream().use { output ->
                    inputStream.copyTo(output)
                }

                file
            } catch (e: Exception) {
                showToast(context, "Ошибка скачивания: ${e.message}")
                null
            }
        }
    }

    fun openFile(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val mimeType = when (file.extension.lowercase()) {
            "pdf" -> "application/pdf"
            else -> "application/octet-stream"
        }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            showToast(context, "Не удалось открыть: ${e.message}")
        }
    }

    private fun showToast(context: Context, message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context.applicationContext, message, Toast.LENGTH_LONG).show()
        }
    }
}