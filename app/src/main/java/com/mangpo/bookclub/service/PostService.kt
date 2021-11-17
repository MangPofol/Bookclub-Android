package com.mangpo.bookclub.service

import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.model.PostDetailModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PostService {
    @GET("/posts")
    suspend fun getPosts(
        @Query("bookId") bookId: Int,
        @Query("clubId") clubId: Int?
    ): Response<PostDetailModel>

    @POST("/posts")
    suspend fun createPost(@Body newPost: PostModel): Response<PostDetailModel>

    @Multipart
    @PUT("/files/upload-multiple-files")
    suspend fun uploadMultiImgFile(@Part data: List<MultipartBody.Part>): Response<List<String>>

    @Multipart
    @PUT("/files/upload")
    suspend fun uploadImgFile(@Part data: MultipartBody.Part): Response<String>
}