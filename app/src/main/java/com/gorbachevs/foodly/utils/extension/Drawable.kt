package com.gorbachevs.foodly.utils.extension

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.gorbachevs.foodly.utils.Constants.DEFAULT_TAG
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

const val SHARED_IMAGE_FILENAME = "shared_image.jpg"
const val FILE_PROVIDER_AUTHORITY = "com.gorbachevs.foodly.FileProvider"
const val COMPRESSION_QUALITY = 100

fun Bitmap.save(context: Context): Uri {
    val cacheDir = context.cacheDir

    val imageFile = File(cacheDir, SHARED_IMAGE_FILENAME)
    return try {
        val outputStream = FileOutputStream(imageFile)
        compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY, outputStream)
        outputStream.flush()
        outputStream.close()

        FileProvider.getUriForFile(
            context,
            FILE_PROVIDER_AUTHORITY,
            imageFile
        )
    } catch (e: IOException) {
        Log.e(DEFAULT_TAG, "Bitmap.save: $e")
        Uri.EMPTY
    }
}
