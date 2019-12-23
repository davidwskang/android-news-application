package com.example.quicknewsapp.main_fragments.bookmarks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.quicknewsapp.models.Article

@Dao
interface BookmarkedArticlesDao {

    @Query("SELECT * FROM article ORDER BY bookmarkedDate DESC")
    fun getAll(): List<Article>

    @Query("SELECT * FROM article WHERE title LIKE :articleTitle")
    fun getArticleByTitle(articleTitle : String) : Article?

    @Insert
    fun insert(article : Article)

    @Delete
    fun delete(article : Article)

}