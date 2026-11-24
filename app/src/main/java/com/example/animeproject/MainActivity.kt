package com.example.animeproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.animeproject.data.network.RetrofitClient
import com.example.animeproject.presentation.screens.*
import com.example.animeproject.presentation.viewmodel.*
import com.example.animeproject.ui.theme.AnimeProjectTheme
import java.net.URLDecoder
import java.net.URLEncoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.init(this)
        setContent {
            AnimeProjectTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainApp()
                }
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val container = remember { (context.applicationContext as AnimeApplication).container }
    val factory = remember { AnimeViewModelFactory(container) }
    val hasFilters by container.filterCache.hasActiveFilters.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Список") },
                    label = { Text("Список") },
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        navController.navigate("list") { popUpTo("list") { inclusive = true } }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Избранное") },
                    label = { Text("Избранное") },
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        navController.navigate("favorites") { popUpTo("favorites") { inclusive = true } }
                    }
                )
                NavigationBarItem(
                    icon = {
                        BadgedBox({
                            if (hasFilters) Badge { Text("·") }
                        }) {
                            Icon(Icons.Default.Settings, contentDescription = "Настройки")
                        }
                    },
                    label = { Text("Настройки") },
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        navController.navigate("settings") { popUpTo("settings") { inclusive = true } }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Профиль") },
                    label = { Text("Профиль") },
                    selected = selectedTab == 3,
                    onClick = {
                        selectedTab = 3
                        navController.navigate("profile") { popUpTo("profile") { inclusive = true } }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "list",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("list") {
                val vm: AnimeListViewModel = viewModel(factory = factory)
                AnimeListScreen(vm, { navController.navigate("details/$it") })
            }
            composable("favorites") {
                val vm: FavoritesViewModel = viewModel(factory = factory)
                FavoritesScreen(vm, { navController.navigate("details/$it") })
            }
            composable("settings") {
                val vm: SettingsViewModel = viewModel(factory = factory)
                SettingsScreen(vm, { navController.popBackStack() })
            }
            composable("profile") {
                val vm: ProfileViewModel = viewModel(factory = factory)
                val profile by vm.profile.collectAsState()
                ProfileScreen(
                    viewModel = vm,
                    onEditClick = { navController.navigate("edit_profile") },
                    onResumeClick = {
                        if (profile.resumeUrl.isNotBlank()) {
                            val encoded = java.net.URLEncoder.encode(profile.resumeUrl, "UTF-8")
                            navController.navigate("pdf_viewer/$encoded")
                        }
                    }
                )
            }
            composable("edit_profile") {
                val vm: EditProfileViewModel = viewModel(factory = factory)
                EditProfileScreen(
                    viewModel = vm,
                    onDone = { navController.popBackStack() },
                    onCancel = { navController.popBackStack() }
                )
            }
            composable(
                "pdf_viewer/{pdfUrl}",
                arguments = listOf(navArgument("pdfUrl") { type = NavType.StringType })
            ) { backStackEntry ->
                val encodedUrl = backStackEntry.arguments?.getString("pdfUrl") ?: ""
                val pdfUrl = URLDecoder.decode(encodedUrl, "UTF-8")
                PdfViewerScreen(
                    pdfUrl = pdfUrl,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(
                "details/{animeId}",
                arguments = listOf(navArgument("animeId") { type = NavType.StringType })
            ) { backStackEntry ->
                val animeId = backStackEntry.arguments?.getString("animeId") ?: ""
                val vm: AnimeDetailsViewModel = viewModel(
                    factory = AnimeViewModelFactory(container, animeId)
                )
                AnimeDetailsScreen(vm, { navController.popBackStack() })
            }
        }
    }
}