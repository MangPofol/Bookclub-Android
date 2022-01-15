package com.mangpo.bookclub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mangpo.bookclub.model.PostDetailModel
import com.mangpo.bookclub.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostViewModel(private val repository: PostRepository) : ViewModel() {
    private val _post: MutableLiveData<PostDetailModel> = MutableLiveData()
    private val _totalCnt: MutableLiveData<Int> = MutableLiveData()
    private val _deletePostCode: MutableLiveData<Int> = MutableLiveData()   //deletePost 결과코드

    private var postDetail: PostDetailModel? = null

    val post: LiveData<PostDetailModel> get() = _post
    val totalCnt: LiveData<Int> get() = _totalCnt
    val deletePostCode: LiveData<Int> get() = _deletePostCode   //deletePost 결과코드

    fun setPost(post: PostDetailModel) {
        _post.value = post
    }

    fun getPost(): PostDetailModel? = _post.value

    fun setPostDetail(postDetail: PostDetailModel?) {
        this.postDetail = postDetail
    }

    fun setDeletePostCode(deletePostCode: Int) {
        _deletePostCode.value = deletePostCode
    }

    suspend fun getPosts(bookId: Long, clubId: Long?): List<PostDetailModel>? =
        repository.getPosts(bookId, clubId)

    suspend fun createPost(post: PostDetailModel): PostDetailModel? {
        val job = viewModelScope.async(Dispatchers.Main) {
            repository.createPost(post)
        }

        postDetail = job.await()

        return postDetail
    }

    suspend fun updatePost(postId: Long, post: PostDetailModel): Boolean {

        val isUpdate = viewModelScope.async {
            when (repository.updatePost(postId, post!!)) {
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

    suspend fun deleteImg(img: String): Int {
        val code = viewModelScope.async {
            repository.deleteImg(img)
        }

        return code.await()
    }

    suspend fun deleteMultiImg(imgList: List<String>) {
        viewModelScope.launch {
            repository.deleteMultiImg(imgList)
        }
    }

    suspend fun deletePost(postId: Long) {
        viewModelScope.launch {
            _deletePostCode.value = repository.deletePost(postId)
        }
    }
}