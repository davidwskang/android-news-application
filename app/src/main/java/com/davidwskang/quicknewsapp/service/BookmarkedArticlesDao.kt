package com.davidwskang.quicknewsapp.service

import androidx.lifecycle.LiveData
import androidx.room.*
import com.davidwskang.quicknewsapp.model.Article
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface BookmarkedArticlesDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Article): Completable

    @Delete
    fun delete(article: Article): Completable

    @Query("SELECT * FROM article ORDER BY bookmarkedDate DESC")
    fun getAll(): LiveData<List<Article>>

    @Query("SELECT * FROM article ORDER BY bookmarkedDate DESC")
    fun getArticles(): Flowable<List<Article>>

    @Query("SELECT * FROM article WHERE title = :title")
    fun getByTitle(title: String): LiveData<Article?>
}