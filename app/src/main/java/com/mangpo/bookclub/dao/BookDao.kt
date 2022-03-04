package com.mangpo.bookclub.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mangpo.bookclub.model.entities.BookEntity

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg book: BookEntity)

    @Query("SELECT image FROM book_table WHERE isbn = :isbn")
    fun getImageByIsbn(isbn: String): String
}