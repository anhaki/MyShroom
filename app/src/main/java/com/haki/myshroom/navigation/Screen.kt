package com.haki.myshroom.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Admin : Screen("admin")
    object Klasifikasi : Screen("klasifikasi")
    object Splash : Screen("splash")
    object History : Screen("history/{name}") {
        fun createRoute(name: String) = "history/$name"
    }

    object Hasil : Screen("hasil/{name}/{confidence}/{timeCost}/{fileName}") {
        fun createRoute(name: String, confidence: Float = 0f, timeCost: Long = 0L, fileName: String = "") =
            "hasil/$name/$confidence/$timeCost/$fileName"
    }
}