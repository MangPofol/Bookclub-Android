package com.example.bookclub.model

import com.google.gson.annotations.SerializedName

data class ClubResData(
    @SerializedName("data")
    var clubs: MutableList<ClubModel>
)
