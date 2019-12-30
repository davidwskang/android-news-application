package com.davidwskang.quicknewsapp.service

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.davidwskang.quicknewsapp.model.Article

class BookmarkedArticlesViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: Repository = Repository(application)
    private var bookmarkedArticles: LiveData<List<Article>>

    init {
        bookmarkedArticles = repository.getAllBookmarks()
    }

    fun insert(article: Article) = repository.insertBookmark(article)

    fun delete(article: Article) = repository.deleteBookmark(article)

    fun getAll() = bookmarkedArticles

    fun getArticleByTitle(title: String) = repository.getBookmarkByTitle(title)

}