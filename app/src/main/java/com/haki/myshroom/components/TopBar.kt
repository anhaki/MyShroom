package com.haki.myshroom.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haki.myshroom.R
import com.haki.myshroom.ui.theme.BlueMain
import com.haki.myshroom.ui.theme.CreamMain
import com.haki.myshroom.ui.theme.RedMain
import com.haki.myshroom.utils.Raleway

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: AnnotatedString,
    onBackClick: (() -> Unit)? = null, // Pass a lambda for back button click handling
    actions: @Composable (() -> Unit)? = null,
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 15.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    color = CreamMain,
                )
            }
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back), // Replace with your back icon resource
                        contentDescription = "Back Button",
                        tint = CreamMain
                    )
                }
            }
        },
        actions = {
            if (actions != null) {
                actions()
            }
        },
        colors = TopAppBarColors(
            containerColor = RedMain,
            titleContentColor = CreamMain,
            actionIconContentColor = CreamMain,
            navigationIconContentColor = CreamMain,
            scrolledContainerColor = BlueMain
        ),

        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onBackClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 15.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    color = CreamMain,
                    fontFamily = Raleway,
                    fontWeight = FontWeight.Normal
                )
            }
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back), // Replace with your back icon resource
                        contentDescription = "Back Button",
                        tint = CreamMain
                    )
                }
            }
        },
        colors = TopAppBarColors(
            containerColor = RedMain,
            titleContentColor = CreamMain,
            actionIconContentColor = CreamMain,
            navigationIconContentColor = CreamMain,
            scrolledContainerColor = BlueMain
        )
    )
}
