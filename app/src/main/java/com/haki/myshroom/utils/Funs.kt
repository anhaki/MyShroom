package com.haki.myshroom.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun AnnotatedString.Builder.appendAnnotatedText(
    text: String,
    color: Color,
    fontWeight: FontWeight,
    fontSize: Int,
    annotationTag: String? = null
) {
    withStyle(
        style = SpanStyle(
            color = color,
            fontSize = fontSize.sp,
            fontFamily = Raleway,
            fontWeight = fontWeight,
        )
    ) {
        annotationTag?.let {
            pushStringAnnotation(tag = it, annotation = it)
        }
        append(text)
    }
}

fun saveImageToFolder(context: Context, bitmap: Bitmap, folderName: String, email: String) {
    val emailFolder = File(context.getExternalFilesDir(null), email)
    if (!emailFolder.exists()) {
        emailFolder.mkdirs()
    }

    val folder = File(emailFolder, folderName)
    if (!folder.exists()) {
        folder.mkdirs()
    }

    val file = File(folder, "image_${System.currentTimeMillis()}.jpg")
    try {
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        Toast.makeText(context, "Gambar berhasil disimpan", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Gagal menyimpan gambar", Toast.LENGTH_LONG).show()
    }
}

fun loadImagesWithNamesFromFolder(context: Context, folderName: String, email: String): List<Pair<String, Bitmap>> {
    val imageList = mutableListOf<Pair<String, Bitmap>>()
    val emailFolder = File(context.getExternalFilesDir(null), email)
    val folder = File(emailFolder, folderName)

    if (folder.exists() && folder.isDirectory) {
        val files = folder.listFiles { file -> file.extension.equals("jpg", ignoreCase = true) }
        if (!files.isNullOrEmpty()) {
            for (file in files) {
                try {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    if (bitmap != null) {
                        imageList.add(Pair(file.name, bitmap))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    return imageList
}

fun compressBitmapIfNeeded(bitmap: Bitmap): Bitmap {
    val maxDimension = 6000
    val width = bitmap.width
    val height = bitmap.height

    return if (width > maxDimension || height > maxDimension) {
        val scaleFactor = maxDimension / maxOf(width, height).toFloat()
        Bitmap.createScaledBitmap(
            bitmap,
            (width * scaleFactor).toInt(),
            (height * scaleFactor).toInt(),
            true
        )
    } else {
        bitmap
    }
}

fun getTimestampFromFileName(fileName: String): String? {
    val pattern = "image_(\\d+)\\.jpg".toRegex()
    val matchResult = pattern.find(fileName)
    return if (matchResult != null) {
        val timestamp = matchResult.groupValues[1].toLongOrNull()
        if (timestamp != null) {
            val date = Date(timestamp)
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            dateFormat.format(date)
        } else {
            null
        }
    } else {
        null
    }
}

fun formatDate(dateString: String, inputFormat: String = "yyyy-MM-dd", outputFormat: String = "dd-MM-yyyy"): String {
    return try {
        val sdfInput = SimpleDateFormat(inputFormat, Locale.getDefault())
        val sdfOutput = SimpleDateFormat(outputFormat, Locale.getDefault())
        val date = sdfInput.parse(dateString)
        date?.let { sdfOutput.format(it) } ?: "-"
    } catch (e: Exception) {
        "-"
    }
}
