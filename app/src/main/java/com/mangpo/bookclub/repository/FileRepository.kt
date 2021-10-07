package com.mangpo.bookclub.repository

import android.util.Log
import com.mangpo.bookclub.service.FileService
import com.mangpo.bookclub.util.ImgUtil
import okhttp3.MultipartBody
import retrofit2.Call
import java.lang.Exception
import retrofit2.Callback
import retrofit2.Response

class FileRepository {
    private val fileService: FileService = ApiClient.fileService

    fun uploadMultipleFiles(imgPaths: List<String>): MutableList<String> {
        var resBody: MutableList<String> = arrayListOf()

        try {
            val multipartBodyList: MutableList<MultipartBody.Part> = ImgUtil.getImagesMultipartBody("data", imgPaths)

            fileService.uploadMultipleFiles(multipartBodyList).enqueue(object: Callback<ArrayList<String>> {
                override fun onResponse(call: Call<ArrayList<String>>, response: Response<ArrayList<String>>) {

                    if (response.code()==200) {
                        Log.e("FileRepository", "성공! -> ${response.body()}")
                        resBody = response.body()!!
                    } else {
                        Log.e("FileRepository", "실패! -> ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ArrayList<String>>, t: Throwable) {
                    Log.e("FileRepository", "uploadMultipleFiles error! -> ${t.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("FileRepository", "uploadMultipleFilew 에러 -> ${e.toString()}")
        }

        return resBody
    }

    suspend fun uploadFile(imgPath: String): MutableList<String> {

        var urlList: MutableList<String> = arrayListOf()

        try {
            val multipartBody: MultipartBody.Part = ImgUtil.getImageMultipartBody("data", imgPath)
            val response = fileService.uploadFile(multipartBody)

            if (response.isSuccessful) {
                urlList.add(response.body()!!)
                Log.e("FileRepository", "uploadFile 성공 -> \n${urlList}")
            } else {
                Log.e("FileRepository", "uploadFile 실패 -> \n${response.code()}\n${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("FileRepository", "uploadFile 에러 -> \n${e.message}")
        }

        return urlList
    }
}