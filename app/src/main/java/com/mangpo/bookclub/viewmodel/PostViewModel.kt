package com.mangpo.bookclub.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.repository.PostRespository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class PostViewModel(): ViewModel() {
    private val postRepository: PostRespository = PostRespository()

    private val _imgUriList: MutableLiveData<List<Uri>?> =
        MutableLiveData<List<Uri>?>()  //사용자가 기록하기에 넣을 이미지

    val imgUriList: MutableLiveData<List<Uri>?> get() = _imgUriList

    fun updateImgUriList(uriList: List<Uri>?) {
        _imgUriList.postValue(uriList)
    }

    suspend fun createPost(newPost: PostModel): PostModel? {
        val newPost = viewModelScope.async (Dispatchers.IO) {
            return@async postRepository.createPost(newPost)
        }.await()

        return newPost
    }
}