package com.haki.myshroom.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import com.haki.myshroom.ui.theme.BlueMain
import com.haki.myshroom.ui.theme.CreamMain

@Composable
fun CircularLoading() {
    CircularProgressIndicator(
//        modifier = Modifier.fillMaxWidth(),
        color = CreamMain,
        trackColor = BlueMain,
    )
}
