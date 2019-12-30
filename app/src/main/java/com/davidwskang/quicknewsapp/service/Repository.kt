package com.davidwskang.quicknewsapp.service

import android.app.Application
import androidx.lifecycle.LiveData
import com.davidwskang.quicknewsapp.model.Article
import com.davidwskang.quicknewsapp.model.SearchItem
import io.reactivex.Completable

class Repository(application: Application) {

    private var bookmarkedArticleDao: BookmarkedArticlesDao
    private var bookmarkedArticles: LiveData<List<Article>>

    private var searchItemDao: SearchItemDao
    private var searchItems : LiveData<List<SearchItem>>

    init {
        val db = ApplicationDatabase.getInstance(application)

        bookmarkedArticleDao = db.bookmarkedArticlesDao()
        bookmarkedArticles = bookmarkedArticleDao.getAll()

        searchItemDao = db.searchItemDao()
        searchItems = searchItemDao.getAll()
    }

    fun insertBookmark(article: Article): Completable {
        return bookmarkedArticleDao.insert(article)
    }

    fun deleteBookmark(article: Article): Completable {
        return bookmarkedArticleDao.delete(article)
    }

    fun getAllBookmarks(): LiveData<List<Article>> {
        return bookmarkedArticles
    }

    fun getBookmarkByTitle(title: String): LiveData<Article?> {
        return bookmarkedArticleDao.getByTitle(title);
    }

    fun insertSearchItem(searchItem : SearchItem) : Completable {
        return searchItemDao.insert(searchItem)
    }

    fun deleteSearchItem(searchItem : SearchItem): Completable {
        return searchItemDao.delete(searchItem)
    }

    fun getAllSearchItems() : LiveData<List<SearchItem>> {
        return searchItems
    }

}