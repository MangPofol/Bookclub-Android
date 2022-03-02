package com.mangpo.bookclub.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object ImgUtils {
    fun prepareFilePart(key: String, fileUri: String): MultipartBody.Part {
        val file: File = File(fileUri)
        val requestFile: RequestBody = RequestBody.create(MediaType.parse("image/jpg"), file)
        return MultipartBody.Part.createFormData(key, file.name, requestFile)
    }

    fun uriToBitmap(context: Context, uri: Uri): Bitmap {
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }

        return bitmap
    }

    fun getAbsolutePathByBitmap(context: Context, bitmap: Bitmap): String {
        val path = "${(context.applicationInfo.dataDir + File.separator + System.currentTimeMillis())}.jpg"
        val file = File(path)
        var out: OutputStream? = null

        try {
            file.createNewFile()
            out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out)
        } finally {
            out?.close()
        }

        return file.absolutePath
    }
}