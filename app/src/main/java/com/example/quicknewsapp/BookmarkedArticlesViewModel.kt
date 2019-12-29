package com.example.quicknewsapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.quicknewsapp.main_fragments.bookmarks.BookmarkedArticlesDao
import com.example.quicknewsapp.models.Article
import io.reactivex.Completable

class BookmarkedArticlesViewModel(application: Application) : AndroidViewModel(application)  {

    var repository : Repository
    var bookmarkedArticles : LiveData<List<Article>>

    init {
        repository = Repository(application)
        bookmarkedArticles = repository.getAll()
    }

    fun insert(article: Article) : Completable {
        return repository.insert(article)
    }

    fun delete(article: Article) : Completable {
        return repository.delete(article)
    }

    fun getAll() : LiveData<List<Article>> {
        return bookmarkedArticles
    }

}