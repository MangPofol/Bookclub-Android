package com.mangpo.bookclub.repository

import android.util.Log
import com.mangpo.bookclub.model.PostDetailModel
import com.mangpo.bookclub.service.PostService
import com.mangpo.bookclub.util.ImgUtil
import okhttp3.MultipartBody

class PostRepository(private val postService: PostService) {

    suspend fun getPosts(bookId: Long, clubId: Long?): List<PostDetailModel>? {

        return try {
            val response = postService.getPosts(bookId, clubId)

            if (response.isSuccessful && response.code() == 200)
                response.body()!!.data
            else {
                Log.e("PostRepository-getPost", response.code().toString())
                null
            }
        } catch (e: Exception) {
            Log.e("PostRepository-getPost", "Fail! -> ${e.message}")
            null
        }
    }

    /*suspend fun createPost(newPost: PostModel): PostDetailModel? {
        val response = postService.createPost(newPost)

        return if (!response.isSuccessful) {
            Log.e(
                "PostRepository",
                "createPost 실패\ncode: ${response.code()}\nerror: ${response.message()}\npost: $newPost"
            )

            null
        } else {
            response.body()
        }
    }*/

    suspend fun createPost(newPost: PostDetailModel): PostDetailModel? {
        val response = postService.createPost(newPost)

        return if (!response.isSuccessful) {
            Log.e(
                "PostRepository",
                "createPost 실패\ncode: ${response.code()}\nerror: ${response.message()}\npost: $newPost"
            )

            null
        } else {
            response.body()
        }
    }

    /*suspend fun updatePost(postId: Long, post: PostModel): Int =
        postService.updatePost(postId, post).code()*/
    suspend fun updatePost(postId: Long, post: PostDetailModel): Int =
        postService.updatePost(postId, post).code()

    suspend fun uploadImgFile(imgPath: String): String? {
        val multipartBody: MultipartBody.Part = ImgUtil.getImageMultipartBody("data", imgPath)
        val response = postService.uploadImgFile(multipartBody)

        return if (response.isSuccessful) {
            response.body()!!
        } else {
            Log.e(
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

    suspend fun getTotalPostCnt(): Int {
        val result = postService.getTotalPostCnt()

        return if (result.isSuccessful)
            result.body()!!.get("data").asInt
        else
            -1
    }

    suspend fun deleteImg(img: String): Int {
        val result = postService.deleteImgFile(img)
        Log.d("PostRepository", "deleteImg code -> ${result.code()}")

        return result.code()
    }

    suspend fun deleteMultiImg(imgList: List<String>) {
        val result = postService.deleteMultiImgFile(imgList)
        Log.d("PostRepository", "deleteMultiImg code -> ${result.code()}")
    }

}