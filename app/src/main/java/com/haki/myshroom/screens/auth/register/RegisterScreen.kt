package com.haki.myshroom.screens.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.haki.myshroom.components.AskAccountComponent
import com.haki.myshroom.components.CustomButton
import com.haki.myshroom.components.CustomPasswordTextField
import com.haki.myshroom.components.CustomTextFieldIcon
import com.haki.myshroom.navigation.Screen
import com.haki.myshroom.screens.auth.AuthViewModel
import com.haki.myshroom.states.AuthState
import com.haki.myshroom.ui.theme.CreamMain
import com.haki.myshroom.ui.theme.RedMain
import com.haki.myshroom.utils.Raleway
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val authState = viewModel.authState.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(authState.value) {
        when (val state = authState.value) {
            is AuthState.Authenticated -> navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Register.route) { inclusive = true }
                isLoading = false
            }

            is AuthState.Loading -> {
                isLoading = true
            }

            is AuthState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = state.message,
                        actionLabel = "OK",
                        duration = SnackbarDuration.Short,
                    )
                }
                viewModel.resetState()
                isLoading = false
            }

            else -> {
                isLoading = false
            }
        }
    }

    Register(
        snackbarHostState = snackbarHostState,
        navController = navController,
        isLoading = isLoading,
        register = { viewModel.register(email, name, password, confirmPassword) },
        email = email,
        onEmailChange = { email = it },
        name = name,
        onNameChange = { name = it },
        password = password,
        onPasswordChange = { password = it },
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = { confirmPassword = it },
    )
}

@Composable
fun Register(
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    navController: NavHostController = rememberNavController(),
    isLoading: Boolean = false,
    register: () -> Unit = {},
    email: String = "",
    onEmailChange: (String) -> Unit = {},
    name: String = "",
    onNameChange: (String) -> Unit = {},
    password: String = "",
    onPasswordChange: (String) -> Unit = {},
    confirmPassword: String = "",
    onConfirmPasswordChange: (String) -> Unit = {},
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CreamMain)
                .padding(28.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Register",
                fontFamily = Raleway,
                fontWeight = FontWeight.Bold,
                color = RedMain,
                fontSize = 49.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(25.dp))

            Column {
                Spacer(modifier = Modifier.height(10.dp))
                CustomTextFieldIcon(
                    labelValue = "Email",
                    icon = Icons.Outlined.Email,
                    textValue = email,
                    onTextChange = onEmailChange,
                )
                CustomTextFieldIcon(
                    labelValue = "Name",
                    icon = Icons.Outlined.Person,
                    textValue = name,
                    onTextChange = onNameChange,
                    maxChar = 25,
                )
                CustomPasswordTextField(
                    labelValue = "Password",
                    icon = Icons.Outlined.Lock,
                    password = password,
                    onTextChange = onPasswordChange,
                )
                CustomPasswordTextField(
                    labelValue = "Confirm Password",
                    icon = Icons.Outlined.Lock,
                    password = confirmPassword,
                    onTextChange = onConfirmPasswordChange,
                    isLast = true,
                )
                CustomButton(
                    action = "Register",
                    onClick = register,
                    isLoading = isLoading,
                )
                AskAccountComponent(
                    textQuery = "Already Have an Account? ",
                    textClickable = "Login",
                    navController
                )
            }
        }

    }

}

@Preview
@Composable
private fun PreviewRegister() {
    Register()
}