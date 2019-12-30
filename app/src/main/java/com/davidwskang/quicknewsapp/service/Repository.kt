package com.davidwskang.quicknewsapp.service

import android.app.Application
import androidx.lifecycle.LiveData
import com.davidwskang.quicknewsapp.model.Article
import com.davidwskang.quicknewsapp.model.SearchItem

class Repository(application: Application) {

    private var bookmarkedArticleDao: BookmarkedArticlesDao
    private var bookmarkedArticles: LiveData<List<Article>>

    private var searchItemDao: SearchItemDao
    private var searchItems: LiveData<List<SearchItem>>

    init {
        val db = ApplicationDatabase.getInstance(application)

        bookmarkedArticleDao = db.bookmarkedArticlesDao()
        bookmarkedArticles = bookmarkedArticleDao.getAll()

        searchItemDao = db.searchItemDao()
        searchItems = searchItemDao.getAll()
    }

    fun insertBookmark(article: Article) = bookmarkedArticleDao.insert(article)

    fun deleteBookmark(article: Article) = bookmarkedArticleDao.delete(article)

    fun getAllBookmarks() = bookmarkedArticles

    fun getBookmarkByTitle(title: String) = bookmarkedArticleDao.getByTitle(title)

    fun insertSearchItem(searchItem: SearchItem) = searchItemDao.insert(searchItem)

    fun deleteSearchItem(searchItem: SearchItem) = searchItemDao.delete(searchItem)

    fun getAllSearchItems() = searchItems

    fun deleteAllSearchItems() = searchItemDao.deleteAll()

}