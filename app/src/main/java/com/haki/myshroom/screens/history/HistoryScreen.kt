package com.haki.myshroom.screens.history

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.haki.myshroom.ui.theme.BlueMain
import com.haki.myshroom.ui.theme.CreamMain
import com.haki.myshroom.ui.theme.RedMain
import com.haki.myshroom.utils.getTimestampFromFileName
import com.haki.myshroom.utils.loadImagesWithNamesFromFolder
import java.io.File
import java.io.FileOutputStream

@Composable
fun HistoryScreen(
    navController: NavHostController,
    viewModel: HistoryViewModel = hiltViewModel(),
    name: String,
) {
    var email by remember { mutableStateOf("") }
    var isPremium by remember { mutableStateOf(false) }
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
                isPremium = it.data.isPremium
                History(
                    title = name,
                    navController = navController,
                    onBackClick = { navController.popBackStack() },
                    email = email,
                    isPremium = isPremiumState.value == true,
                )
            }

            null -> {

            }
        }
    }
}

@Composable
fun History(
    modifier: Modifier = Modifier,
    title: String,
    navController: NavHostController,
    onBackClick: () -> Unit,
    email: String,
    isPremium: Boolean,
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopBar(title, onBackClick = onBackClick)
        },
        modifier = modifier
    ) { innerPadding ->
        var imageList by remember { mutableStateOf<List<Pair<String, Bitmap>>>(emptyList()) }

        LaunchedEffect(title) {
            imageList = loadImagesWithNamesFromFolder(context, title, email)
        }
        if (isPremium) {
            if (imageList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CreamMain)
                        .padding(start = 10.dp, end = 10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Anda belum menyimpan gambar $title pada perangkat ini.",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = BlueMain
                    )
                }
            } else {
                LazyVerticalGrid(
                    modifier = Modifier
                        .background(CreamMain)
                        .fillMaxHeight(),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        start = 10.dp,
                        end = 10.dp,
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding()
                    )
                ) {
                    items(imageList)
                    { item ->
                        val (fileName, bitmap) = item

                        Column (
                            modifier = Modifier
                                .padding(10.dp)
                                .clip(RoundedCornerShape(12))
                                .background(RedMain)
                                .clickable {
                                    val path = context.getExternalFilesDir(null)!!.absolutePath

                                    val tempFile = File(path, "tempFileName.jpg")
                                    val fOut = FileOutputStream(tempFile)
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                                    fOut.close()

                                    navController.navigate(
                                        Screen.Hasil.createRoute(
                                            name = title,
                                            fileName = fileName,
                                        )
                                    )
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Image(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .padding(top = 10.dp, start = 10.dp, end = 10.dp,)
                                    .clip(RoundedCornerShape(10)),
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "$title image",
                                contentScale = ContentScale.Crop,
                            )
                            getTimestampFromFileName(fileName)?.let {
                                val date = it.substringBefore(" ")
                                val time = it.substringAfter(" ")

                                Text(
                                    modifier = Modifier
                                        .padding(top = 5.dp,),
                                    text = date,
                                    color = CreamMain,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(bottom = 5.dp,),
                                    text = time,
                                    color = CreamMain,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CreamMain)
                    .padding(start = 10.dp, end = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Anda belum premium, silahkan upgrade agar bisa menyimpan.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = BlueMain
                )
            }
        }
    }
}