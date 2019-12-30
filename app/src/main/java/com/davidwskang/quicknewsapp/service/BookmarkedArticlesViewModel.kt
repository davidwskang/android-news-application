package com.davidwskang.quicknewsapp.service

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.davidwskang.quicknewsapp.model.Article
import io.reactivex.Completable

class BookmarkedArticlesViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: Repository = Repository(application)
    private var bookmarkedArticles: LiveData<List<Article>>

    init {
        bookmarkedArticles = repository.getAllBookmarks()
    }

    fun insert(article: Article): Completable {
        return repository.insertBookmark(article)
    }

    fun delete(article: Article): Completable {
        return repository.deleteBookmark(article)
    }

    fun getAll(): LiveData<List<Article>> {
        return bookmarkedArticles
    }

    fun getArticleByTitle(title: String): LiveData<Article?> {
        return repository.getBookmarkByTitle(title)
    }

}