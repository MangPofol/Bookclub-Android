package com.mangpo.bookclub.service

import com.google.gson.JsonObject
import com.mangpo.bookclub.model.AllPostModel
import com.mangpo.bookclub.model.PostDetailModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PostService {
    @GET("/posts")
    suspend fun getPosts(
        @Query("bookId") bookId: Long,
        @Query("clubId") clubId: Long?
    ): Response<AllPostModel>

    @GET("/posts/total-count")
    suspend fun getTotalPostCnt(): Response<JsonObject>

    @POST("/posts")
    suspend fun createPost(@Body newPost: PostDetailModel): Response<PostDetailModel>

    @Multipart
    @POST("/files/upload-multiple-files")
    suspend fun uploadMultiImgFile(@Part data: List<MultipartBody.Part>): Response<List<String>>

    @POST("/files/delete-multiple-files")
    suspend fun deleteMultiImgFile(@Body imageLocations: List<String>): Response<String>

    @PUT("/posts/{postId}")
    suspend fun updatePost(
        @Path("postId") postId: Long,
        @Body post: PostDetailModel
    ): Response<String>

    @Multipart
    @PUT("/files/upload")
    suspend fun uploadImgFile(@Part data: MultipartBody.Part): Response<String>

    @DELETE("/files/delete")
    suspend fun deleteImgFile(@Query("imageLocation") imageLocation: String): Response<String>

    @DELETE("/posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: Long): Response<Int>
}