package com.haki.myshroom.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.haki.myshroom.ui.theme.BlueMain
import com.haki.myshroom.ui.theme.CreamMain
import com.haki.myshroom.ui.theme.RedMain

@Composable
fun LogoutConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(RedMain)) {
                Text(text = "Yes", color = CreamMain)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(BlueMain)) {
                Text(text = "No", color = CreamMain)
            }
        },
        text = {
            Text(text = "Are you sure you want to log out?", fontSize = 18.sp)
        },
        containerColor = CreamMain,
        textContentColor = BlueMain
    )
}
