package com.davidwskang.quicknewsapp.search

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchItem::class], version = 1, exportSchema = false)
public abstract class SearchItemDatabase : RoomDatabase() {
    public abstract fun searchItemDao() : SearchItemDao
}