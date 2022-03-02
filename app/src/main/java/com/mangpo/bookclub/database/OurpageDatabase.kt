package com.mangpo.bookclub.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mangpo.bookclub.dao.BookDao
import com.mangpo.bookclub.model.entities.BookEntity

@Database(entities = [BookEntity::class], version = 1, exportSchema = false)
abstract class OurpageDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: OurpageDatabase? = null

        fun getDatabase(context: Context): OurpageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OurpageDatabase::class.java,
                    "ourpage_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}