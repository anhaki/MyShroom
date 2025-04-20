package com.haki.myshroom.screens.klasifikasi

import android.graphics.Bitmap
import android.os.SystemClock
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.haki.myshroom.R
import com.haki.myshroom.classifier.Recognition
import com.haki.myshroom.classifier.toNamedLabel
import com.haki.myshroom.components.TopBar
import com.haki.myshroom.navigation.Screen
import com.haki.myshroom.states.ResultState
import com.haki.myshroom.ui.theme.BlueMain
import com.haki.myshroom.ui.theme.CreamMain
import com.haki.myshroom.ui.theme.RedMain
import com.haki.myshroom.utils.compressBitmapIfNeeded
import java.io.File
import java.io.FileOutputStream

@Composable
fun KlasifikasiScreen(
    navController: NavHostController,
    viewModel: KlasifikasiViewModel = hiltViewModel(),
) {
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }
    val hasil by viewModel.result.collectAsState()
    var isPremium by remember { mutableStateOf(false) }
    val userSession = viewModel.userSession.observeAsState()
    val isPremiumState = viewModel.isPremiumState.observeAsState()

    userSession.value.let { result ->
        when (result) {
            is ResultState.Error -> {

            }

            is ResultState.Loading -> {

            }

            is ResultState.Success -> {
                isPremium = result.data.isPremium
                Klasifikasi(
                    title = "Klasifikasi",
                    onBackClick = { navController.popBackStack() },
                    navController = navController,
                    onClassify = { bitmap, start -> bitmap?.let { viewModel.classify(it, start) } },
                    bitmapState = bitmapState,
                    result = hasil,
                    resetResultState = { viewModel.resetResultState() },
                    isPremium = isPremiumState.value == true
                )
            }

            null -> {

            }
        }
    }
}

@Composable
fun Klasifikasi(
    title: String,
    onBackClick: () -> Unit,
    navController: NavHostController,
    onClassify: (Bitmap?, Long) -> Unit,
    bitmapState: MutableState<Bitmap?>,
    result: ResultState<Recognition>,
    resetResultState: () -> Unit,
    isPremium: Boolean,
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { capturedBitmap ->
        bitmapState.value = capturedBitmap
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            bitmapState.value = bitmap
        }
    }

    when (result) {
        is ResultState.Loading -> {
            Text("Loading...", Modifier.padding(vertical = 16.dp))
        }

        is ResultState.Success -> {
            val path = context.getExternalFilesDir(null)!!.absolutePath

            val tempFile = File(path, "tempFileName.jpg")
            val fOut = FileOutputStream(tempFile)

            val compressedBitmap = bitmapState.value?.let { compressBitmapIfNeeded(it) }

            compressedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.close()

            val namedLabel = result.data.toNamedLabel()

            navController.navigate(
                Screen.Hasil.createRoute(
                    namedLabel.namedLabel,
                    namedLabel.confidence,
                    namedLabel.timeCost
                )
            )

            resetResultState()
        }

        is ResultState.Error -> {

        }
    }

    Scaffold(
        topBar = { TopBar(title, onBackClick = onBackClick) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamMain)
                .padding(innerPadding),
        ) {
            LazyColumn(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 50.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    if (bitmapState.value != null) {
                        val previewBitmap = bitmapState.value?.let { compressBitmapIfNeeded(it) }
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            contentScale = ContentScale.Crop,
                            bitmap = previewBitmap!!.asImageBitmap(),
                            contentDescription = "Preview",
                        )
                    } else {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            painter = painterResource(R.drawable.ic_image),
                            contentDescription = "Image",
                        )
                    }
                }
                item {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                    ) {
                        Button(
                            onClick = { launcher.launch() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(BlueMain),
                            enabled = isPremium,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_camera),
                                tint = CreamMain,
                                contentDescription = "Camera Icon",
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                                    .size(30.dp),
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { galleryLauncher.launch("image/*") },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(BlueMain),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_gallery),
                                tint = CreamMain,
                                contentDescription = "Gallery Icon",
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                                    .size(30.dp),
                            )
                        }
                    }
                }
                item {
                    Button(
                        onClick = {
                            val start = SystemClock.uptimeMillis()
                            onClassify(bitmapState.value, start)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(RedMain),
                        enabled = bitmapState.value != null,
                    ) {
                        Text(
                            "Klasifikasi",
                            modifier = Modifier.padding(vertical = 10.dp),
                            fontSize = 18.sp,
                            color = CreamMain
                        )
                    }
                }
            }
        }
    }
}
