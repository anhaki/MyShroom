package com.haki.myshroom.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.haki.myshroom.navigation.Screen
import com.haki.myshroom.ui.theme.BlueMain
import com.haki.myshroom.ui.theme.RedMain
import com.haki.myshroom.utils.appendAnnotatedText

@Composable
fun AskAccountComponent(
    textQuery: String,
    textClickable: String,
    navController: NavHostController
) {
    val annotatedString = buildAnnotatedString {
        appendAnnotatedText(textQuery, BlueMain, FontWeight.Medium, fontSize = 13)
        appendAnnotatedText(
            textClickable,
            RedMain,
            FontWeight.Bold,
            annotationTag = textClickable,
            fontSize = 13
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ClickableText(
            text = annotatedString,
            onClick = {
                annotatedString.getStringAnnotations(it, it)
                    .firstOrNull()?.also { annonation ->
                        if (annonation.item == "Login") {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                        } else if (annonation.item == "Register") {
                            navController.navigate(Screen.Register.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    }
            })
    }
}