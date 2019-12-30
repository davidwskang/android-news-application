package com.davidwskang.quicknewsapp.view.fragment.main_fragments.search.view

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.davidwskang.quicknewsapp.view.fragment.main_fragments.AppMainFragment
import com.davidwskang.quicknewsapp.model.Constants
import com.davidwskang.quicknewsapp.R
import com.davidwskang.quicknewsapp.view.fragment.main_fragments.feed.SearchFeedFragment
import com.davidwskang.quicknewsapp.service.SearchItemDatabase
import com.davidwskang.quicknewsapp.model.SearchItem
import com.davidwskang.quicknewsapp.view.fragment.main_fragments.search.view.SearchHistoryAdapter.OnRecentSearchClickedListener
import com.davidwskang.quicknewsapp.util.AppUtils
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import org.joda.time.DateTime
import timber.log.Timber
import java.util.*

class SearchFragment : AppMainFragment(), OnRecentSearchClickedListener {

    override val layoutId = R.layout.fragment_search
    override val fragmentIndex = Constants.SEARCH_INDEX

    override var actionBarTitle = Constants.SEARCH_TITLE
    override var actionBarIconId = R.drawable.ic_search_black_35dp
    override var actionBarSearchVisibility = View.VISIBLE

    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    companion object {
        fun newInstance(): SearchFragment {
            val args = Bundle()
            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchHistoryAdapter = SearchHistoryAdapter(this)
        recent_search_list.adapter = searchHistoryAdapter
        recent_search_list.layoutManager = LinearLayoutManager(context)
        loadSearchHistory()
    }

    override fun onSearchItemClicked(searchItem: SearchItem) = doSearch(searchItem.searchTerm)

    override fun onSearchItemRemoveClicked(searchItem: SearchItem) = deleteSearchHistoryItem(searchItem)

    override fun onClearHistoryClicked() = deleteAllSearchHistory()

    fun doSearch(searchTerm: String) {

        mainActivity?.getAppBar()?.windowToken?.run {
            AppUtils.hideSoftKeyboard(context, this)
        }

        showSearchResults(searchTerm)

        val searchItem = SearchItem(searchTerm = searchTerm, date = DateTime.now().millis)
        insertToSearchHistory(searchItem)
    }

    fun showSearchHistory() {
        val frags = childFragmentManager.fragments
        if (frags.isEmpty()) return

        // show search history
        val resultsFrag = frags[0]
        loadSearchHistory(Action {
            childFragmentManager.beginTransaction()
                    .remove(resultsFrag)
                    .commit()
            updateActionBar()
        })
    }

    private fun showSearchResults(searchTerm: String) {
        val frag = SearchFeedFragment.newInstance(
            searchTerm = searchTerm,
                apiKey = mainActivity!!.API_KEY
        )

        childFragmentManager.beginTransaction()
                .add(R.id.results_fragment_container, frag)
                .show(frag)
                .commit()

        updateActionBar(searchTerm)
    }

    private fun updateActionBar(searchTerm: String = "") {
        if (searchTerm.isEmpty()) {
            actionBarTitle = Constants.SEARCH_TITLE
            actionBarIconId = R.drawable.ic_search_black_35dp
            actionBarSearchVisibility = View.VISIBLE
        } else {
            actionBarTitle = searchTerm
            actionBarIconId = R.drawable.ic_arrow_back_black_35dp
            actionBarSearchVisibility = View.GONE
        }
        mainActivity!!.updateActionBar(this)
    }

    private fun insertToSearchHistory(searchItem: SearchItem) {
        val observable = object : Completable() {
            override fun subscribeActual(observer: CompletableObserver) {
                try {
                    getSearchDatabase().searchItemDao().insert(searchItem)
                    observer.onComplete()
                } catch (e : Exception) {
                    observer.onError(e)
                }
            }
        }
        val observer = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe({
                    updateSearchHistoryView()
                },{ Timber.e(it) })

        compositeDisposable.add(observer)
    }

    private fun loadSearchHistory(callback : Action? = null) {
        val observable = object : Single<ArrayList<SearchItem>>() {
            override fun subscribeActual(observer: SingleObserver<in ArrayList<SearchItem>>) {
                try {
                    val searchItems = getSearchDatabase().searchItemDao().getSearches()
                    if(searchItems == null) {
                        observer.onError(Exception())
                    } else {
                        observer.onSuccess(searchItems as ArrayList<SearchItem>)
                    }
                } catch (e : Exception) {
                    observer.onError(e)
                }
            }
        }
        val observer = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe({
                    searchHistoryAdapter.setRecentSearchItems(it)
                    updateSearchHistoryView()
                    callback?.run()
                },{
                    Timber.e(it)
                })

        compositeDisposable.add(observer)
    }

    private fun deleteSearchHistoryItem(searchItem: SearchItem) {
        val observable = object : Completable() {
            override fun subscribeActual(observer: CompletableObserver) {
                try {
                    getSearchDatabase().searchItemDao().delete(searchItem)
                    observer.onComplete()
                } catch (e : Exception) {
                    observer.onError(e)
                }
            }
        }
        val observer = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe({
                    updateSearchHistoryView()
                }, { Timber.e(it) })
        compositeDisposable.add(observer)
    }

    private fun deleteAllSearchHistory() {
        val observable = object : Completable() {
            override fun subscribeActual(observer: CompletableObserver) {
                try {
                    getSearchDatabase().searchItemDao().deleteAll()
                    observer.onComplete()
                } catch (e : Exception) {
                    observer.onError(e)
                }
            }
        }
        val observer = observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe({
                    searchHistoryAdapter.emptySearchHistory()
                    updateSearchHistoryView()
                },{ Timber.e(it) })
        compositeDisposable.add(observer)
    }

    private fun updateSearchHistoryView() {
        if (searchHistoryAdapter.itemCount == 0) {
            empty_search_list_message.visibility = View.VISIBLE
            recent_search_list.visibility = View.GONE
        } else {
            recent_search_list.visibility = View.VISIBLE
            empty_search_list_message.visibility = View.GONE
        }
    }

    private fun getSearchDatabase() : SearchItemDatabase {
        return Room.databaseBuilder(activity!!.applicationContext,
                SearchItemDatabase::class.java, Constants.SEARCH_DB).build()
    }
}