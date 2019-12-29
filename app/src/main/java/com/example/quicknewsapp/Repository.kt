package com.example.quicknewsapp

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.quicknewsapp.main_fragments.bookmarks.BookmarkedArticlesDao
import com.example.quicknewsapp.main_fragments.bookmarks.BookmarkedArticlesDatabase
import com.example.quicknewsapp.models.Article
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class Repository(application: Application) {

    var bookmarkedArticleDao : BookmarkedArticlesDao
    var bookmarkedArticles : LiveData<List<Article>>

    init {
        val db = BookmarkedArticlesDatabase.getInstance(application)
        bookmarkedArticleDao = db.bookmarkedArticlesDao()
        bookmarkedArticles = bookmarkedArticleDao.getAllArticles()
    }

    fun insert(article : Article) : Completable {
        return bookmarkedArticleDao.insertArticle(article)
    }

    fun delete(article : Article) : Completable {
        return bookmarkedArticleDao.deleteArticle(article)
    }

    fun getAll() : LiveData<List<Article>> {
        return bookmarkedArticles
    }

}