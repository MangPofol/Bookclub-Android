package com.mangpo.bookclub.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mangpo.bookclub.model.PostDetailModel
import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class PostViewModel(private val repository: PostRepository) : ViewModel() {
    private val _imgs: MutableLiveData<List<Bitmap>> = MutableLiveData()

    private var post: PostModel? = null
    private var postDetail: PostDetailModel? = null

    val imgs: LiveData<List<Bitmap>> get() = _imgs


    fun setImgs(bitmaps: List<Bitmap>) {
        _imgs.value = bitmaps
    }

    fun removeImg(position: Int) {
        (_imgs.value as ArrayList).removeAt(position)
    }

    fun clearImg() {
        if (_imgs.value != null)
            (_imgs.value as ArrayList).clear()
    }

    fun setPost(post: PostModel?) {
        this.post = post
    }

    fun getPost(): PostModel? = this.post

    fun getPostDetail(): PostDetailModel? = this.postDetail

    suspend fun createPost(post: PostModel): PostDetailModel? {
        val job = viewModelScope.async(Dispatchers.Main) {
            repository.createPost(post)
        }

        postDetail = job.await()

        return postDetail
    }

    suspend fun uploadImg(imgPath: String): String? {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            repository.uploadImgFile(imgPath)
        }
    }

    suspend fun uploadMultiImg(imgPaths: List<String>): List<String>? {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            repository.uploadMultiImgFile(imgPaths)
        }
    }
}