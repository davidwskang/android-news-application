package com.davidwskang.quicknewsapp.service

import androidx.lifecycle.LiveData
import androidx.room.*
import com.davidwskang.quicknewsapp.model.SearchItem
import io.reactivex.Completable

@Dao
public interface SearchItemDao {

    @Query("SELECT * FROM searchItem")
    fun getAll(): LiveData<List<SearchItem>>

    @Query("SELECT * FROM searchItem ORDER BY date DESC")
    fun getSearches() : List<SearchItem>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(searchItem : SearchItem) : Completable

    @Delete
    fun delete(searchItem: SearchItem?) : Completable

    @Query("DELETE FROM searchitem")
    fun deleteAll()

}