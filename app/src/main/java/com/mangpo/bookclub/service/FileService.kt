package com.mangpo.bookclub.service

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface FileService {
    @Multipart
    @PUT("/files/upload-multiple-files")
    fun uploadMultipleFiles(@Part data: List<MultipartBody.Part>):Call<ArrayList<String>>

    @Multipart
    @PUT("/files/upload")
    suspend fun uploadFile(@Part data: MultipartBody.Part):Response<String>
}