package com.mangpo.bookclub.repository

import android.util.Log
import com.mangpo.bookclub.model.PostDetailModel
import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.service.PostService
import com.mangpo.bookclub.util.ImgUtil
import okhttp3.MultipartBody

class PostRepository(private val postService: PostService) {

    suspend fun createPost(newPost: PostModel): PostDetailModel? {
        val response = postService.createPost(newPost)

        return if (!response.isSuccessful) {
            Log.e(
                "PostRepository",
                "createPost 실패\ncode: ${response.code()}\nerror: ${response.message()}"
            )

            null
        } else {
            response.body()
        }
    }

    suspend fun uploadImgFile(imgPath: String): String? {
        val multipartBody: MultipartBody.Part = ImgUtil.getImageMultipartBody("data", imgPath)
        val response = postService.uploadImgFile(multipartBody)

        return if (response.isSuccessful) {
            response.body()!!
        } else {
            Log.d(
                "PostRepository",
                "uploadImgFile 실패\ncode: ${response.code()}\nerror: ${response.message()}"
            )
            null
        }
    }

    suspend fun uploadMultiImgFile(imgPaths: List<String>): List<String>? {
        val multipartBodyList: MutableList<MultipartBody.Part> =
            ImgUtil.getImagesMultipartBody("data", imgPaths)
        val response = postService.uploadMultiImgFile(multipartBodyList)

        return if (response.isSuccessful) {
            response.body()
        } else {
            Log.d(
                "PostRepository",
                "uploadMultiImgFile 실패\ncode: ${response.code()}\nerror: ${response.message()}"
            )
            null
        }
    }
    /*suspend fun getPost(bookId: Int, clubId: Int?): ArrayList<PostModel>? {

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
    }*/
}