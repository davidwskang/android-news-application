package com.davidwskang.quicknewsapp.search

import androidx.lifecycle.LiveData
import androidx.room.*
import com.davidwskang.quicknewsapp.search.SearchItem
import io.reactivex.Completable

@Dao
public interface SearchItemDao {

    @Query("SELECT * FROM SearchItems ORDER BY date DESC")
    fun getAll(): LiveData<List<SearchItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(searchItem: SearchItem): Completable

    @Delete
    fun delete(searchItem: SearchItem?): Completable

    @Query("DELETE FROM SearchItems")
    fun deleteAll(): Completable

}