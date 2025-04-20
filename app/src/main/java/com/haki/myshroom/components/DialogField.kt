package com.haki.myshroom.components

import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DialogField(
    modifier: Modifier = Modifier,
    labelValue: String,
    textValue: String,
    onTextChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(text = "Add New Group")
        },
        text = {
            CustomTextField(
                labelValue = labelValue,
                textValue = textValue,
                onTextChange = onTextChange
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    // Handle "Ok" click
                    onConfirm(textValue)
                }
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    // Handle "Cancel" click
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}
