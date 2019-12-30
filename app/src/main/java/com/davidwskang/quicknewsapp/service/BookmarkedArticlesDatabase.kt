package com.davidwskang.quicknewsapp.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.davidwskang.quicknewsapp.model.Article
import com.davidwskang.quicknewsapp.model.Constants
import com.davidwskang.quicknewsapp.model.Converter

@Database(entities = [Article::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class BookmarkedArticlesDatabase : RoomDatabase() {

    companion object {
        private var instance : BookmarkedArticlesDatabase? = null

        @Synchronized
        fun getInstance(context : Context) : BookmarkedArticlesDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                        BookmarkedArticlesDatabase::class.java,
                        Constants.SAVED_DB)
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance!!
        }
    }

    abstract fun bookmarkedArticlesDao() : BookmarkedArticlesDao



}