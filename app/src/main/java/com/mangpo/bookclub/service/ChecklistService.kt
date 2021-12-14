package com.mangpo.bookclub.service

import com.mangpo.bookclub.model.ChecklistsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ChecklistService {
    @GET("/todos")
    fun getChecklists(): Response<ChecklistsModel>

    @POST("/create-todos")
    fun createChecklist(checklist: List<String>): Response<String>
}