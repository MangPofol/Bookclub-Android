package com.mangpo.bookclub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mangpo.bookclub.model.entities.RecordRequest
import com.mangpo.bookclub.model.entities.RecordUpdateRequest
import com.mangpo.bookclub.model.remote.RecordResponse
import com.mangpo.bookclub.repository.PostRepositoryImpl

class PostViewModel: BaseViewModel() {
    private val postRepository: PostRepositoryImpl = PostRepositoryImpl()

    private val _newRecord: MutableLiveData<RecordResponse> = MutableLiveData()
    val newRecord: LiveData<RecordResponse> get() = _newRecord

    private val _uploadImgPaths: MutableLiveData<List<String>> = MutableLiveData()
    val uploadImgPaths: LiveData<List<String>> get() = _uploadImgPaths

    private val _deletePostCode: MutableLiveData<Int> = MutableLiveData()
    val deletePostCode: LiveData<Int> get() = _deletePostCode

    private val _deletePhotosCode: MutableLiveData<Int> = MutableLiveData()
    val deletePhotosCode: LiveData<Int> get() = _deletePhotosCode

    private val _updatePostCode: MutableLiveData<Int> = MutableLiveData()
    val updatePostCode: LiveData<Int> get() = _updatePostCode

    fun createRecord(record: RecordRequest) {
        postRepository.createPost(
            record = record,
            onResponse = {
                _newRecord.value = it.body()
            },
            onFailure = {
                _newRecord.value = null
            }
        )
    }

    fun uploadImgPaths(imgPaths: List<String>) {
        postRepository.uploadImgFile(
            imgPaths = imgPaths,
            onResponse = {
                _uploadImgPaths.value = it.body()
            },
            onFailure = {
                _uploadImgPaths.value = null
            }
        )
    }

    fun deletePost(recordId: Int) {
        postRepository.deletePost(
            postId = recordId,
            onResponse = {
                _deletePostCode.value = it.code()
            },
            onFailure = {
                _deletePostCode.value = 600
            }
        )
    }

    fun deleteMultiplePhotos(deletePhotos: List<String>) {
        postRepository.deletePhotos(
            deletePhotos = deletePhotos,
            onResponse = {
                _deletePhotosCode.value = it.code()
            },
            onFailure = {
                _deletePhotosCode.value = 600
            }
        )
    }

    fun updatePost(recordId: Int, updateRecord: RecordUpdateRequest) {
        postRepository.updatePost(
            postId = recordId,
            updateRecord = updateRecord,
            onResponse = {
                _updatePostCode.value = it.code()
            },
            onFailure = {
                _updatePostCode.value = 600
            }
        )
    }
}