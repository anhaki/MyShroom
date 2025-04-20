package com.haki.myshroom.screens.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haki.myshroom.components.LogoutConfirmationDialog
import com.haki.myshroom.components.PremiumDialog
import com.haki.myshroom.components.TopBar
import com.haki.myshroom.navigation.Screen
import com.haki.myshroom.states.ResultState
import com.haki.myshroom.ui.theme.BlueMain
import com.haki.myshroom.ui.theme.CreamMain
import com.haki.myshroom.ui.theme.RedMain
import com.haki.myshroom.utils.Raleway
import com.haki.myshroom.utils.appendAnnotatedText
import com.haki.myshroom.utils.drawableMap
import com.haki.myshroom.utils.mushroomSafetyMap
import com.haki.myshroom.utils.mushrooms
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    var name by remember { mutableStateOf("") }
    var isPremium by remember { mutableStateOf(false) }
    val userSession = viewModel.userSession.observeAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showPremiumDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    userSession.value.let {
        LaunchedEffect(it) {
            when (it) {
                is ResultState.Success -> {
                    name = it.data.name
                    isPremium = it.data.isPremium
                }

                else -> {}
            }
        }
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

    if (showPremiumDialog) {
        PremiumDialog(
            onDismiss = { showPremiumDialog = false },
            onWhatsapp = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/+62895394796746"))
                context.startActivity(intent)
            }
        )
    }

    Home(
        title = buildAnnotatedString {
            appendAnnotatedText("Welcome, ", CreamMain, FontWeight.Medium, 20)
            appendAnnotatedText(name, CreamMain, FontWeight.Bold, fontSize = 20)
            appendAnnotatedText("!", CreamMain, FontWeight.Medium, fontSize = 20)
        },
        navController = navController,
        onLogoutClick = { showLogoutDialog = true },
        onPremiumClick = { showPremiumDialog = true },
        isPremium = isPremium,
    )
}


@Composable
fun Home(
    modifier: Modifier = Modifier,
    title: AnnotatedString,
    isPremium: Boolean,
    navController: NavHostController,
    onLogoutClick: () -> Unit,
    onPremiumClick: () -> Unit,
) {
    var sortOption by remember { mutableStateOf("name") }

    Scaffold(
        topBar = {
            TopBar(
                title = title,
                actions = {
                    Row {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout Icon",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable(onClick = onLogoutClick),
                            tint = CreamMain
                        )
                        Spacer(Modifier.width(10.dp))
                        if (!isPremium) {
                            Icon(
                                imageVector = Icons.Outlined.Star,
                                contentDescription = "Premium Icon",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable(onClick = onPremiumClick),
                                tint = CreamMain
                            )
                        }
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.Klasifikasi.route)
                },
                containerColor = RedMain,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(50))
            ) {
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.size(80.dp)
                ) {
                    drawCircle(color = CreamMain)
                }
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = RedMain,
                    modifier = Modifier.size(50.dp)
                )
            }

        },
        modifier = modifier
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
                        text = "Collection",
                        fontFamily = Raleway,
                        fontWeight = FontWeight.Bold,
                        color = BlueMain,
                        fontSize = 36.sp,
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Sort by:",
                            fontFamily = Raleway,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = BlueMain
                        )
                        Row {
                            Text(
                                text = "Name",
                                modifier = Modifier
                                    .clickable { sortOption = "name" }
                                    .padding(horizontal = 8.dp),
                                color = if (sortOption == "name") RedMain else BlueMain,
                                fontWeight = if (sortOption == "name") FontWeight.Bold else FontWeight.Normal
                            )
                            Text(
                                text = "Safety",
                                modifier = Modifier
                                    .clickable { sortOption = "safety" }
                                    .padding(horizontal = 8.dp),
                                color = if (sortOption == "safety") RedMain else BlueMain,
                                fontWeight = if (sortOption == "safety") FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            val sortedMushrooms = when (sortOption) {
                "name" -> mushrooms.sortedBy { it }
                "safety" -> mushrooms.sortedBy { mushroomSafetyMap[it] }
                else -> mushrooms
            }

            items(items = sortedMushrooms) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(RedMain)
                        .clickable {
                            navController.navigate(Screen.History.createRoute(name = item))
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val drawableId = drawableMap[item]

                    if (drawableId != null) {
                        Image(
                            modifier = Modifier
                                .width(80.dp)
                                .height(80.dp),
                            contentScale = ContentScale.Crop,
                            painter = painterResource(drawableId),
                            contentDescription = item
                        )
                    } else {
                        Text("Image not found for $item")
                    }
                    Column(
                        modifier = Modifier
                            .padding(15.dp)
                    ) {
                        Text(
                            text = item,
                            fontSize = 20.sp,
                            fontFamily = Raleway,
                            fontWeight = FontWeight.Bold,
                            color = CreamMain,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = mushroomSafetyMap[item].toString(),
                            fontSize = 16.sp,
                            fontFamily = Raleway,
                            color = CreamMain,
                        )
                    }
                }
            }

        }
    }
}