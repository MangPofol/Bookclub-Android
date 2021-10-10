package com.mangpo.bookclub.repository

import android.util.Log
import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.model.PostReqModel
import com.mangpo.bookclub.model.PostResModel
import com.mangpo.bookclub.service.PostService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.HTTP

class PostRespository {
    private val postService: PostService = ApiClient.postService

    suspend fun createPost(newPost: PostReqModel): PostModel? {
        Log.e("PostRepository", newPost.toString())
        val response = postService.createPost(newPost)

        try {
            if (response.isSuccessful) {
                return response.body()
            } else {
                Log.e("PostRepository", "createPost 실패 -> \n${response.code()}\n${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "createPost 중 에러 발생 -> \n${e.message}")
        }

        return null
    }

    suspend fun getPost(bookId: Int, clubId: Int?): ArrayList<PostModel>? {

        return try {
            val response = postService.getPosts(bookId, clubId)

            if (response.isSuccessful && response.code()==200)
                response.body()!!.posts
            else {
                Log.e("PostRepository-getPost", response.code().toString())
                null
            }
        } catch (e: Exception) {
            Log.e("PostRepository-getPost", "Fail! -> ${e.message}")
            null
        }
    }
}