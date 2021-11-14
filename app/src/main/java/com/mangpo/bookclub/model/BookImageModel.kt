package com.mangpo.bookclub.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookImageModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "isbn") val isbn: String,
    @ColumnInfo(name = "image") val image: String = ""
)
