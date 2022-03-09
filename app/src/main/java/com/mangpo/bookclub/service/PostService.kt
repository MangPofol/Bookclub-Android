package com.mangpo.bookclub.service

import com.mangpo.bookclub.model.entities.RecordRequest
import com.mangpo.bookclub.model.entities.RecordUpdateRequest
import com.mangpo.bookclub.model.remote.RecordResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface PostService {
    @POST("/posts")
    fun createPost(@Body record: RecordRequest): Call<RecordResponse>
    @Multipart
    @POST("/files/upload-multiple-files")
    fun uploadImgFile(@Part data: List<MultipartBody.Part>): Call<List<String>>
    @POST("/files/delete-multiple-files")
    fun deleteMultipleFiles(@Body deletePhotos: List<String>): Call<Void>

    @PUT("/posts/{postId}")
    fun updatePost(@Path("postId") postId: Int, @Body updateRecord: RecordUpdateRequest): Call<RecordResponse>

    @DELETE("/posts/{postId}")
    fun deletePost(@Path("postId") postId: Int): Call<Void>
}