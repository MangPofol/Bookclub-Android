package com.mangpo.bookclub.service

import com.mangpo.bookclub.model.CheckListModel
import com.mangpo.bookclub.model.ChecklistsModel
import retrofit2.Response
import retrofit2.http.*

interface ChecklistService {
    @GET("/todos")
    suspend fun getChecklists(): Response<ChecklistsModel>

    @POST("/todos")
    suspend fun createChecklist(@Body checklist: CheckListModel): Response<CheckListModel>

    @PUT("/todos/{toDoId}")
    suspend fun updateChecklist(
        @Path("toDoId") toDoId: Long,
        @Body checklist: CheckListModel
    ): Response<String>
}