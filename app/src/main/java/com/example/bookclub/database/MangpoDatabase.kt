package com.example.bookclub.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookclub.dao.BookImageDao
import com.example.bookclub.model.BookImageModel

@Database(entities = [BookImageModel::class], version = 1)
abstract class MangpoDatabase: RoomDatabase() {
    abstract fun bookImageDao(): BookImageDao

    companion object {
        private var instance: MangpoDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MangpoDatabase? {
            if (instance==null) {
                synchronized(MangpoDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MangpoDatabase::class.java,
                        "mangpo-database"
                    ).build()
                }
            }

            return instance
        }
    }
}