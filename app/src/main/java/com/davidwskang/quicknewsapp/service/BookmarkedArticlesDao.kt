package com.davidwskang.quicknewsapp.service

import androidx.lifecycle.LiveData
import androidx.room.*
import com.davidwskang.quicknewsapp.model.Article
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface BookmarkedArticlesDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(article: Article): Completable

    @Delete
    fun deleteArticle(article: Article): Completable

    @Query("SELECT * FROM article ORDER BY bookmarkedDate DESC")
    fun getAllArticles(): LiveData<List<Article>>

    @Query("SELECT * FROM article ORDER BY bookmarkedDate DESC")
    fun getArticles(): Flowable<List<Article>>

    @Query("SELECT * FROM article WHERE title = :title")
    fun getArticleByTitle(title: String): LiveData<Article?>
}