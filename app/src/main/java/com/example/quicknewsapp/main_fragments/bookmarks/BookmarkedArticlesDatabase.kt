package com.example.quicknewsapp.main_fragments.bookmarks

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quicknewsapp.models.Article
import com.example.quicknewsapp.models.Converter

@Database(entities = [Article::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class BookmarkedArticlesDatabase : RoomDatabase() {
    abstract fun savedArticlesDao() : BookmarkedArticlesDao
}