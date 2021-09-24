package com.mangpo.bookclub.service

import com.mangpo.bookclub.model.ClubModel
import com.mangpo.bookclub.model.ClubResData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ClubService {
    @GET("/clubs")
    suspend fun getClubsByUser(): Response<ClubResData>

    @POST("/clubs")
    suspend fun createClub(@Body newClub: ClubModel): Response<ClubModel>
}