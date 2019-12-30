package com.davidwskang.quicknewsapp.main_fragments.search

import androidx.room.*

@Dao
public interface SearchItemDao {

    @Query("SELECT * FROM searchItem")
    fun getAll(): List<SearchItem>?

    @Query("SELECT * FROM searchItem ORDER BY date DESC")
    fun getSearchesInOrder() : List<SearchItem>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearch(searchItem : SearchItem)

    @Delete
    fun deleteSearch(searchItem: SearchItem?)

    @Query("DELETE FROM searchitem")
    fun deleteAllSearches()

}