package com.mangpo.bookclub.util

import android.net.Uri
import okhttp3.MultipartBody
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ImgUtilTest {
    @Test
    fun getImagesMultipartBody() {
        var bodyPartList : MutableList<MultipartBody.Part> = arrayListOf()
        val uriList: List<Uri> = listOf(
            Uri.parse("content://media/external/images/media/13459"),
            Uri.parse("content://media/external/images/media/13462"))

        bodyPartList = ImgUtil.getImagesMultipartBody("data", uriList)

        println("1: ${bodyPartList[0].body}")
        println("2: ${bodyPartList[1]}")
    }

    @Test
    fun getImageMultipartBodyTest() {
        val result = ImgUtil.getImageMultipartBody("data", Uri.parse("content://media/external/images/media/13459"))
        println(result.body)
    }
}