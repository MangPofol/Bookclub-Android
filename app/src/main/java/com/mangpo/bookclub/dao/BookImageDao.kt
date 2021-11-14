package com.mangpo.bookclub.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mangpo.bookclub.model.BookImageModel

@Dao
interface BookImageDao {
    @Insert
    fun insertBook(vararg bookImg: BookImageModel)

    @Query("SELECT image FROM BookImageModel WHERE isbn = :isbn")
    fun getImage(isbn: String): String
}