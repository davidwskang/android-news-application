package com.davidwskang.quicknewsapp.service

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.davidwskang.quicknewsapp.model.SearchItem
import io.reactivex.Completable

class SearchItemsViewModel (application: Application) : AndroidViewModel(application) {

    private var repository: Repository = Repository(application)
    private var searchItems: LiveData<List<SearchItem>>

    init {
        searchItems = repository.getAllSearchItems()
    }

    fun insert(searchItem : SearchItem): Completable {
        return repository.insertSearchItem(searchItem)
    }

    fun delete(searchItem : SearchItem): Completable {
        return repository.deleteSearchItem(searchItem)
    }

    fun getAll(): LiveData<List<SearchItem>> {
        return searchItems
    }

}