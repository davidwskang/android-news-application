package com.davidwskang.quicknewsapp.view.fragment.main_fragments.search.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidwskang.quicknewsapp.R
import com.davidwskang.quicknewsapp.model.Constants
import com.davidwskang.quicknewsapp.model.SearchItem
import com.davidwskang.quicknewsapp.service.SearchItemsViewModel
import com.davidwskang.quicknewsapp.util.AppUtils
import com.davidwskang.quicknewsapp.view.fragment.main_fragments.AppMainFragment
import com.davidwskang.quicknewsapp.view.fragment.main_fragments.feed.SearchFeedFragment
import com.davidwskang.quicknewsapp.view.fragment.main_fragments.search.view.SearchHistoryAdapter.OnRecentSearchClickedListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import org.joda.time.DateTime
import timber.log.Timber

class SearchFragment : AppMainFragment(), OnRecentSearchClickedListener {

    override val layoutId = R.layout.fragment_search
    override val fragmentIndex = Constants.SEARCH_INDEX

    override var actionBarTitle = Constants.SEARCH_TITLE
    override var actionBarIconId = R.drawable.ic_search_black_35dp
    override var actionBarSearchVisibility = View.VISIBLE

    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    lateinit var viewModel: SearchItemsViewModel

    companion object {
        fun newInstance(): SearchFragment {
            val args = Bundle()
            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchItemsViewModel::class.java)
        viewModel.getAll().observe(this,
                Observer<List<SearchItem>> {
                    searchHistoryAdapter.setRecentSearchItems(it)

                    if (searchHistoryAdapter.itemCount == 0) {
                        empty_search_list_message.visibility = View.VISIBLE
                        recent_search_list.visibility = View.GONE
                    } else {
                        recent_search_list.visibility = View.VISIBLE
                        empty_search_list_message.visibility = View.GONE
                    }
                })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchHistoryAdapter = SearchHistoryAdapter(this)
        recent_search_list.adapter = searchHistoryAdapter
        recent_search_list.layoutManager = LinearLayoutManager(context)
    }

    override fun onSearchItemClicked(searchItem: SearchItem) = doSearch(searchItem.searchTerm)

    override fun onSearchItemRemoveClicked(searchItem: SearchItem) {
        compositeDisposable.add(viewModel.delete(searchItem)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe({
                    Timber.e("deleted")
                }, {
                    Timber.e(it)
                }))
    }

    override fun onClearHistoryClicked() {
        compositeDisposable.add(viewModel.deleteAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe({}, {}))
    }

    fun doSearch(searchTerm: String) {

        mainActivity?.getAppBar()?.windowToken?.run {
            AppUtils.hideSoftKeyboard(context, this)
        }

        showSearchResults(searchTerm)

        val searchItem = SearchItem(searchTerm = searchTerm, date = DateTime.now().millis)

        compositeDisposable.add(viewModel.insert(searchItem)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe({
                    Timber.e("successfully inserted search")
                }, {
                    Timber.e("error search")
                }))
    }

    fun showSearchHistory() {
        val frags = childFragmentManager.fragments
        if (frags.isEmpty()) return

        childFragmentManager.beginTransaction()
                .remove(frags[0])
                .commit()
        updateActionBar()
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

}