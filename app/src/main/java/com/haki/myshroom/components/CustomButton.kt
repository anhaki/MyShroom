package com.haki.myshroom.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haki.myshroom.ui.theme.RedMain
import com.haki.myshroom.utils.Raleway

@Composable
fun CustomButton(
    action: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
) {
    Spacer(modifier = Modifier.height(10.dp))

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(RedMain),
        enabled = !isLoading,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(40.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularLoading()
            } else {
                Text(
                    text = action,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = Raleway,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}