package com.haki.myshroom.screens.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haki.myshroom.R
import com.haki.myshroom.navigation.Screen
import com.haki.myshroom.preferences.UserPrefModel
import com.haki.myshroom.states.AuthState
import com.haki.myshroom.states.ResultState
import com.haki.myshroom.ui.theme.BlueMain
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val authState = viewModel.authState.observeAsState()
    val userSession = viewModel.userSession.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.updatePremiumEveryOpen()

        delay(3000L)
        when (authState.value) {
            AuthState.Unauthenticated -> navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }

            else -> {
                if(userSession.value is ResultState.Success){
                    if ((userSession.value as ResultState.Success<UserPrefModel>).data.isAdmin){
                        navController.navigate(Screen.Admin.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else{
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .background(color = BlueMain)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 70.dp),
            painter = painterResource(R.drawable.logo_png),
            contentDescription = "Splash Screen",
        )
    }
}