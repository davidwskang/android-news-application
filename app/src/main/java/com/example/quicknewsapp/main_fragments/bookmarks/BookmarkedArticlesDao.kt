package com.example.quicknewsapp.main_fragments.bookmarks

import androidx.room.*
import com.example.quicknewsapp.models.Article
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface BookmarkedArticlesDao {

    @Query("SELECT * FROM article ORDER BY bookmarkedDate DESC")
    fun getAll(): List<Article>

    @Query("SELECT * FROM article WHERE title LIKE :articleTitle")
    fun getArticleByTitle(articleTitle : String) : Article?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article : Article)

    @Delete
    fun delete(article : Article)

}