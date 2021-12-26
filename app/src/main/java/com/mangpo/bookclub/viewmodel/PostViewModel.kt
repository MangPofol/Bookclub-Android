package com.mangpo.bookclub.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mangpo.bookclub.model.PostDetailModel
import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostViewModel(private val repository: PostRepository) : ViewModel() {
    private val _imgUriList: MutableLiveData<List<Uri>> = MutableLiveData()
    private val _post: MutableLiveData<PostModel> = MutableLiveData()
    private val _totalCnt: MutableLiveData<Int> = MutableLiveData()

    private var postDetail: PostDetailModel? = null

    val imgUriList: LiveData<List<Uri>> get() = _imgUriList
    val post: LiveData<PostModel> get() = _post
    val totalCnt: LiveData<Int> get() = _totalCnt

    fun setImgUriList(imgUriList: List<Uri>) {
        _imgUriList.value = imgUriList
    }

    fun getImgUriList(): List<Uri>? = _imgUriList.value

    fun setPost(post: PostModel) {
        _post.value = post
    }

    fun getPost(): PostModel? = _post.value

    fun setPostDetail(postDetail: PostDetailModel?) {
        this.postDetail = postDetail
    }

    fun getPostDetail(): PostDetailModel? = this.postDetail

    suspend fun getPosts(bookId: Long, clubId: Long?): List<PostDetailModel>? =
        repository.getPosts(bookId, clubId)

    suspend fun createPost(post: PostModel): PostDetailModel? {
        val job = viewModelScope.async(Dispatchers.Main) {
            repository.createPost(post)
        }

        postDetail = job.await()

        return postDetail
    }

    suspend fun updatePost(postId: Long, post: PostModel): Boolean {

        val isUpdate = viewModelScope.async {
            val code = repository.updatePost(postId, post!!)

            Log.d("PostViewModel", "updatePost code -> $code")
            when (code) {
                204 -> true
                else -> false
            }
        }

        return isUpdate.await()
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

    suspend fun getTotalPostCnt() {
        viewModelScope.launch {
            _totalCnt.value = repository.getTotalPostCnt()
        }
    }

    suspend fun deleteImg(img: String) {
        viewModelScope.launch {
            repository.deleteImg(img)
        }
    }
}