package com.mangpo.bookclub.service

import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.model.PostReqModel
import com.mangpo.bookclub.model.PostResModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PostService {
    //http://localhost:8080/posts?bookId=3&clubId=15
    @GET("/posts")
    suspend fun getPosts(@Query("bookId") bookId: Int, @Query("clubId") clubId: Int?): Response<PostResModel>

    @POST("/posts")
    suspend fun createPost(@Body newPost: PostReqModel): Response<PostModel>
}