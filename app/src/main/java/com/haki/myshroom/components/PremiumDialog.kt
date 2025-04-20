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
fun PremiumDialog(onDismiss: () -> Unit, onWhatsapp: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(RedMain)) {
                Text(text = "Okay", color = CreamMain)
            }
        },
        dismissButton = {
            Button(onClick = onWhatsapp, colors = ButtonDefaults.buttonColors(BlueMain)) {
                Text(text = "Whatsapp", color = CreamMain)
            }
        },
        text = {
            Text(
                text = "Untuk upgrade ke premium, kamu dapat transfer ke rekening BCA 0213970001 dengan harga:\n\n" +
                        "Rp65.000/Bulan (berlaku kelipatan)\n\n" +
                        "Kamu dapat mengirimkan bukti pembayaran kepada Admin lewat Whatsapp untuk dilakukan verifikasi",
                fontSize = 18.sp
            )
        },
        containerColor = CreamMain,
        textContentColor = BlueMain
    )
}
