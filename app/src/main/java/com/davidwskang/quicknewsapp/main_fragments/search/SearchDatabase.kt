package com.davidwskang.quicknewsapp.main_fragments.search

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchItem::class], version = 1, exportSchema = false)
public abstract class SearchDatabase : RoomDatabase() {
    public abstract fun searchItemDao() : SearchItemDao
}