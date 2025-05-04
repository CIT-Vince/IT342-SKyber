package com.example.skyber.ModularFunctions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import java.io.ByteArrayOutputStream

object ImageUtils {
    fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        } catch (e: Exception) {
            null
        }
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        return try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            ""
        }
    }
}


@Composable
fun Base64Image(
    base64String: String?,
    modifier: Modifier = Modifier
) {
    if (!base64String.isNullOrEmpty()) {
        val bitmap = remember(base64String) {
            ImageUtils.base64ToBitmap(base64String)
        }
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = modifier,
                contentScale = ContentScale.Crop // This makes it fill and crop inside the shape
            )
        }
    } else {
        Text("No image available", modifier = modifier)
    }
}