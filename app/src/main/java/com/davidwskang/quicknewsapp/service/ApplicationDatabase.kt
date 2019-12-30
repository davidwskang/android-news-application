package com.davidwskang.quicknewsapp.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.davidwskang.quicknewsapp.model.Article
import com.davidwskang.quicknewsapp.model.Constants
import com.davidwskang.quicknewsapp.model.Converter
import com.davidwskang.quicknewsapp.model.SearchItem

@Database(entities = [Article::class, SearchItem::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ApplicationDatabase : RoomDatabase() {

    companion object {
        private var instance: ApplicationDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ApplicationDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                        ApplicationDatabase::class.java,
                        Constants.SAVED_DB)
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance!!
        }
    }

    abstract fun bookmarkedArticlesDao(): BookmarkedArticlesDao

    abstract fun searchItemDao(): SearchItemDao

}