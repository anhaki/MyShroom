package com.haki.myshroom.screens.hasil

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haki.myshroom.components.TopBar
import com.haki.myshroom.navigation.Screen
import com.haki.myshroom.states.ResultState
import com.haki.myshroom.ui.theme.CreamMain
import com.haki.myshroom.ui.theme.RedMain
import com.haki.myshroom.utils.Raleway
import com.haki.myshroom.utils.mushroomDescMap
import com.haki.myshroom.utils.mushroomSafetyMap
import com.haki.myshroom.utils.saveImageToFolder
import java.io.File

@Composable
fun HasilScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: HasilViewModel = hiltViewModel(),
    name: String,
    confidence: Float = 0f,
    timeCost: Long = 0L,
    fileName: String = "",
) {
    var email by remember { mutableStateOf("") }
    var isPremium by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val userSession = viewModel.userSession.observeAsState()
    val isPremiumState = viewModel.isPremiumState.observeAsState()

    userSession.value.let {
        when (it) {
            is ResultState.Error -> {

            }

            is ResultState.Loading -> {

            }

            is ResultState.Success -> {
                email = it.data.email
                status = mushroomSafetyMap[name].toString()
                description = mushroomDescMap[name].toString()
                isPremium = isPremiumState.value == true
                Hasil(
                    title = "Jamur $name",
                    onBackClick = { navController.popBackStack() },
                    navController = navController,
                    name = name,
                    status = status,
                    description = description,
                    confidence = confidence,
                    timeCost = timeCost,
                    email = email,
                    isPremium = isPremium,

                )
            }

            null -> {

            }
        }
    }
}

@Composable
fun Hasil(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    title: String,
    onBackClick: () -> Unit,
    name: String,
    status: String,
    description: String,
    email: String,
    confidence: Float = 0f,
    timeCost: Long = 0L,
    isPremium: Boolean,
) {
    val context = LocalContext.current

    val path = context.getExternalFilesDir(null)!!.absolutePath
    val imagePath = "$path/tempFileName.jpg"

    val image = BitmapFactory.decodeFile(imagePath)

    Scaffold(
        topBar = {
            TopBar(
                title,
                onBackClick = onBackClick
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamMain)
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 25.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 30.dp, bottom = 30.dp)
            ) {
                item {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        bitmap = image.asImageBitmap(),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                    )
                }
                item {
                    Column {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = name,
                            fontFamily = Raleway,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = status,
                            fontFamily = Raleway,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            modifier = Modifier.padding(top = 15.dp),
                            text = description,
                            fontFamily = Raleway,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
                if (confidence != 0f && timeCost != 0L) {
                    item {
                        Column {
                            Text(
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .fillMaxWidth(),
                                text = "Waktu prediksi : $timeCost" + "ms",
                                fontFamily = Raleway,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),
                            ) {
                                Button(
                                    onClick = {
                                        File(imagePath).deleteOnExit()

                                        saveImageToFolder(context, image, name, email)
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.Home.route) { inclusive = true }
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(RedMain),
                                    enabled = isPremium
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .padding(vertical = 10.dp),
                                        text = "Simpan",
                                        fontFamily = Raleway,
                                        fontSize = 18.sp,
                                        color = CreamMain,
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Button(
                                    onClick = {
                                        File(imagePath).deleteOnExit()

                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.Home.route) { inclusive = true }
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(RedMain),
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .padding(vertical = 10.dp),
                                        text = "Selesai",
                                        fontFamily = Raleway,
                                        fontSize = 18.sp,
                                        color = CreamMain,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

