package com.davidwskang.quicknewsapp.service

import androidx.room.Database
import androidx.room.RoomDatabase
import com.davidwskang.quicknewsapp.model.SearchItem

@Database(entities = [SearchItem::class], version = 1, exportSchema = false)
public abstract class SearchItemDatabase : RoomDatabase() {
    public abstract fun searchItemDao() : SearchItemDao
}