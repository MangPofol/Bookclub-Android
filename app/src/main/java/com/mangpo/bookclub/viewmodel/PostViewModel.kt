package com.mangpo.bookclub.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.model.PostReqModel
import com.mangpo.bookclub.repository.FileRepository
import com.mangpo.bookclub.repository.PostRespository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostViewModel(): ViewModel() {
    private val postRepository: PostRespository = PostRespository()
    private val fileRepository: FileRepository = FileRepository()

    private val _imgUriList: MutableLiveData<MutableList<Uri>?> =
        MutableLiveData<MutableList<Uri>?>()  //사용자가 기록하기에 넣을 이미지
    private val _temporaryPost: MutableLiveData<PostReqModel?> = MutableLiveData<PostReqModel?>()   //임시로 저장해 놓는 post

    val imgUriList: MutableLiveData<MutableList<Uri>?> get() = _imgUriList
    val temporaryPost: MutableLiveData<PostReqModel?> get() = _temporaryPost

    fun updateImgUriList(uriList: MutableList<Uri>?) {
        _imgUriList.postValue(uriList)
    }

    fun initTemtporaryPost() {
        _temporaryPost.value = PostReqModel()
    }

    fun setTemporaryPost(post: PostReqModel?) {
        _temporaryPost.value = post
    }

    /*suspend fun updateBooksLatestPostDate(books: MutableList<BookModel>) {
        for (book in books) {
            if (book.latestPostDate=="") {
                val posts = viewModelScope.async {
                    getPost(book.id!!.toInt(), null)
                }

                //Log.e("PostViewModel", "updateBooksLatestPostDate\n book name: ${book.name}\n posts: ${posts.await()}")

                if (posts.await()!!.size!=0)
                    book.latestPostDate = posts.await()!![posts.await()!!.size-1].modifiedDate
                else
                    book.latestPostDate = book.modifiedDate
            }
        }
    }*/

    suspend fun createPost(newPost: PostReqModel): PostModel? {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            postRepository.createPost(newPost)
        }
    }

    suspend fun getPost(bookId: Int, clubId: Int?): ArrayList<PostModel>? {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            postRepository.getPost(bookId, clubId)
        }
    }

    fun uploadMultipleImg(paths: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            fileRepository.uploadMultipleFiles(paths)
        }
    }

    suspend fun uploadImg(path: String): MutableList<String> {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            fileRepository.uploadFile(path)
        }
    }
}