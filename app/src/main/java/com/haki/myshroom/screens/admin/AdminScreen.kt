package com.haki.myshroom.screens.admin

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haki.myshroom.classifier.Recognition
import com.haki.myshroom.components.CircularLoading
import com.haki.myshroom.components.LogoutConfirmationDialog
import com.haki.myshroom.components.TopBar
import com.haki.myshroom.database.entity.UserEntity
import com.haki.myshroom.navigation.Screen
import com.haki.myshroom.states.ResultState
import com.haki.myshroom.ui.theme.BlueMain
import com.haki.myshroom.ui.theme.CreamMain
import com.haki.myshroom.ui.theme.RedMain
import com.haki.myshroom.utils.Raleway
import com.haki.myshroom.utils.appendAnnotatedText
import com.haki.myshroom.utils.formatDate
import com.haki.myshroom.components.CustomTextField
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.regex.Pattern

@Composable
fun AdminScreen(
    navController: NavHostController,
    viewModel: AdminViewModel = hiltViewModel(),
) {
    var name by remember { mutableStateOf("") }
    val userSession = viewModel.userSession.observeAsState()
    val user = viewModel.users
    var showLogoutDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val updateResult by viewModel.updateResult.collectAsState()

    userSession.value.let {
        LaunchedEffect(it) {
            when (it) {
                is ResultState.Success -> {
                    name = it.data.name
                }

                else -> {}
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.fetchUsers()
    }

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                coroutineScope.launch {
                    viewModel.logout()
                    showLogoutDialog = false
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    Admin(
        title = buildAnnotatedString {
            appendAnnotatedText("Welcome, ", CreamMain, FontWeight.Medium, 20)
            appendAnnotatedText(name, CreamMain, FontWeight.Bold, fontSize = 20)
            appendAnnotatedText("!", CreamMain, FontWeight.Medium, fontSize = 20)
        },
        user = user.value,
        onLogoutClick = { showLogoutDialog = true },
        updateUser = {email, nama, isPremium,expDate ->
            coroutineScope.launch {
                viewModel.updateUser(email, nama, isPremium,expDate)
            }
        },
        fetchUser = {
            coroutineScope.launch {
                viewModel.fetchUsers()
            }
        },
        updateResult = updateResult,
    )
}

@Composable
    fun Admin(
    modifier: Modifier = Modifier,
    title: AnnotatedString,
    user: List<UserEntity>,
    onLogoutClick: () -> Unit,
    updateUser: (String, String, Boolean, String) -> Unit,
    fetchUser: () -> Unit,
    updateResult: ResultState<UserEntity>,
    ) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    val filteredUsers = user.filter { it.email.contains(searchQuery, ignoreCase = true) }

    when (updateResult) {
        is ResultState.Success -> {
            Log.e("yuwuwu", "haedehh")
            fetchUser()
        }
        is ResultState.Error -> {
            Toast.makeText(context, updateResult.errorMessage, Toast.LENGTH_SHORT).show()
        }

        ResultState.Loading -> {
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = title,
                actions = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = onLogoutClick),
                        tint = CreamMain
                    )
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = modifier,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .background(CreamMain)
                .fillMaxHeight()
                .padding(horizontal = 25.dp),
            contentPadding = PaddingValues(bottom = 130.dp)
        ) {
            item {
                Column {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 43.dp, bottom = 20.dp),
                        text = "Admin",
                        fontFamily = Raleway,
                        fontWeight = FontWeight.Bold,
                        color = BlueMain,
                        fontSize = 36.sp,
                        textAlign = TextAlign.Center
                    )
                    CustomTextField(
                        textValue = searchQuery,
                        onTextChange = { searchQuery = it },
                        labelValue = "Search by email",
                    )
                }
            }

            items(filteredUsers) { item ->
                val dateRegex = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$")

                var showEditDialog by remember { mutableStateOf(false) }
                var editableName by remember { mutableStateOf(item.name) }
                var editablePremiumStatus by remember { mutableStateOf(item.isPremium) }
                var editableDate by remember { mutableStateOf(item.expDate.toString()) }
                var isAbleSave by remember { mutableStateOf(true) }
                val coroutineScope = rememberCoroutineScope()

                if (showEditDialog) {
                    AlertDialog(
                        onDismissRequest = { showEditDialog = false },
                        title = { Text(text = "Edit User Information") },
                        text = {
                            Column {
                                CustomTextField(
                                    textValue = editableName,
                                    onTextChange = { editableName = it },
                                    labelValue = "Name",
                                )
                                Row(
                                    modifier = Modifier
                                        .clickable {
                                            editablePremiumStatus = !editablePremiumStatus
                                            if (!editablePremiumStatus) editableDate = "null"
                                            isAbleSave = validateSaveStatus(
                                                editablePremiumStatus,
                                                editableDate,
                                                dateRegex
                                            )
                                        }
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = editablePremiumStatus,
                                        onCheckedChange = {
                                            editablePremiumStatus = it
                                            if(!editablePremiumStatus) editableDate = "null"
                                            isAbleSave = validateSaveStatus(editablePremiumStatus, editableDate, dateRegex)
                                        },
                                        colors = CheckboxDefaults.colors(RedMain),
                                    )
                                    Text(text = "Premium User")
                                }
                                if (editablePremiumStatus) {
                                    CustomTextField(
                                        textValue = if (editableDate == "null") "" else editableDate,
                                        onTextChange = {
                                            editableDate = it
                                            isAbleSave = validateSaveStatus(editablePremiumStatus, editableDate, dateRegex)
                                        },
                                        labelValue = "Expiry Date (yyyy-MM-dd)",
                                    )
                                }
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (isAbleSave) {
                                        coroutineScope.launch {
                                            updateUser(
                                                item.email,
                                                editableName,
                                                editablePremiumStatus,
                                                if (editablePremiumStatus) editableDate else "null"
                                            )
                                        }
                                        showEditDialog = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(BlueMain),
                                enabled = isAbleSave
                            ) {
                                Text("Save")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showEditDialog = false },
                                colors = ButtonDefaults.buttonColors(RedMain)
                            ) {
                                Text("Cancel")
                            }
                        },
                        containerColor = CreamMain,
                        textContentColor = BlueMain
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        Modifier.weight(1f)
                    ) {
                        Text(
                            text = item.name,
                            fontFamily = Raleway,
                            fontWeight = FontWeight.Normal,
                            fontSize = 17.sp,
                        )
                        Text(
                            text = item.email,
                            fontFamily = Raleway,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        )
                        Row (
                            modifier = Modifier.padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ){
                            Text(
                                modifier = Modifier
                                    .background(if (item.isPremium) Color.Green else Color.Red)
                                    .padding(horizontal = 4.dp, vertical = 2.dp),
                                text = if (item.isPremium) "Premium" else "Non-Premium",
                                fontFamily = Raleway,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = CreamMain,
                            )
                            if(item.isPremium){
                                Text(
                                    text = " s.d ",
                                    fontFamily = Raleway,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = BlueMain
                                )
                                Text(
                                    text = formatDate(item.expDate.toString()),
                                    fontFamily = Raleway,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                )
                            }
                        }
                    }
                    Button(
                        onClick = {
                            showEditDialog = true
                            editableName = item.name
                            editablePremiumStatus = item.isPremium
                            editableDate = item.expDate.toString()
                            isAbleSave = validateSaveStatus(editablePremiumStatus, editableDate, dateRegex)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RedMain)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Icon",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

private fun validateSaveStatus(isPremium: Boolean, expiryDate: String, dateRegex: Pattern): Boolean {
    return if (isPremium) {
        if (!dateRegex.matcher(expiryDate).matches()) {
            false
        } else {
            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val inputDate = LocalDate.parse(expiryDate, formatter)
                val today = LocalDate.now()
                inputDate.isAfter(today)
            } catch (e: DateTimeParseException) {
                false
            }
        }
    } else {
        true
    }
}
