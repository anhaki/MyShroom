package com.haki.myshroom

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.haki.myshroom.navigation.Screen
import com.haki.myshroom.screens.admin.AdminScreen
import com.haki.myshroom.screens.auth.login.LoginScreen
import com.haki.myshroom.screens.auth.register.RegisterScreen
import com.haki.myshroom.screens.hasil.HasilScreen
import com.haki.myshroom.screens.history.HistoryScreen
import com.haki.myshroom.screens.home.HomeScreen
import com.haki.myshroom.screens.klasifikasi.KlasifikasiScreen
import com.haki.myshroom.screens.splash.SplashScreen

@Composable
fun MyShroomNavigation() {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Admin.route) {
            AdminScreen(navController = navController)
        }
        composable(route = Screen.Klasifikasi.route) {
            KlasifikasiScreen(navController = navController)
        }
        composable(
            route = Screen.History.route,
            arguments = listOf(navArgument("name") { type = NavType.StringType }),
        ) {
            val name = it.arguments?.getString("name") ?: ""
            HistoryScreen(
                name = name,
                navController = navController,
            )
        }
        composable(
            route = Screen.Hasil.route,
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("confidence") { type = NavType.FloatType },
                navArgument("timeCost") { type = NavType.LongType },
                navArgument("fileName") { type = NavType.StringType },
            ),
        ) {
            val name = it.arguments?.getString("name") ?: ""
            val confidence = it.arguments?.getFloat("confidence") ?: 0F
            val timeCost = it.arguments?.getLong("timeCost") ?: 0L
            val fileName = it.arguments?.getString("fileName") ?: ""
            HasilScreen(
                name = name,
                confidence = confidence,
                timeCost = timeCost,
                fileName = fileName,
                navController = navController,
            )
        }
    }
}