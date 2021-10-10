package com.mangpo.bookclub.util

import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull

object ImgUtil {

    fun getImagesMultipartBody(key: String, uriList: List<String>): MutableList<MultipartBody.Part>{
        val bodyPartList : MutableList<MultipartBody.Part> = arrayListOf()
        for (uri in uriList) {
            val file = File(uri)
            val requestBody: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body: MultipartBody.Part = MultipartBody.Part.createFormData("data", file.name, requestBody)

            bodyPartList.add(body)
        }

        return bodyPartList
    }

    fun getImageMultipartBody(key: String, uri: String): MultipartBody.Part {
        val file = File(uri)

        return MultipartBody.Part.createFormData("data", file.name, file.asRequestBody("image/*".toMediaType()))
    }
}