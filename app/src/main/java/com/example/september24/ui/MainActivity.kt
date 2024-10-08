package com.example.september24.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.september24.ui.theme.September24Theme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.september24.BuildConfig
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            September24Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val apiKey = BuildConfig.MAPS_API_KEY
                    Places.initialize(applicationContext, apiKey)
                    ReminderApp()
                }
            }
        }
    }
}



@Composable
fun ReminderApp() {
    // Create a NavController to manage navigation
    val navController = rememberNavController()

    // Set up the NavHost with destinations
    NavHost(navController, startDestination = "reminderScreen") {
        composable("reminderScreen") { ReminderScreen(navController) }
        composable(
            "reminderDetail/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) { backStackEntry ->
            ReminderDetailScreen(
                navController,
                id = backStackEntry.arguments?.getLong("id") ?: 0L
            )
        }
    }
}








