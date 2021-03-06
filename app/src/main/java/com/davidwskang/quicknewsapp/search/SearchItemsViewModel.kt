package com.davidwskang.quicknewsapp.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.davidwskang.quicknewsapp.service.Repository

class SearchItemsViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: Repository = Repository(application)
    private var searchItems: LiveData<List<SearchItem>>

    init {
        searchItems = repository.getAllSearchItems()
    }

    fun insert(searchItem: SearchItem) = repository.insertSearchItem(searchItem)

    fun delete(searchItem: SearchItem) = repository.deleteSearchItem(searchItem)

    fun getAll() = searchItems

    fun deleteAll() = repository.deleteAllSearchItems()

}