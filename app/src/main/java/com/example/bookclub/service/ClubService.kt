package com.example.bookclub.service

import com.example.bookclub.model.ClubModel
import com.example.bookclub.model.ClubResData
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