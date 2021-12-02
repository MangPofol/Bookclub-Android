package com.mangpo.bookclub.util

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull

import okhttp3.*

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

        if (!file.exists())
            file.mkdir()

        return MultipartBody.Part.createFormData("data", file.name, file.asRequestBody("image/*".toMediaType()))
    }

}