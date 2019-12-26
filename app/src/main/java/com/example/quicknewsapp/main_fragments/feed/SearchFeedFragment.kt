package com.example.quicknewsapp.main_fragments.feed

import android.os.Bundle
import com.example.quicknewsapp.api.NewsApi
import com.example.quicknewsapp.api.RetrofitClient
import com.example.quicknewsapp.models.Feed
import io.reactivex.Single

class SearchFeedFragment : FeedFragment() {

    lateinit var searchTerm: String

    companion object {
        @JvmStatic
        fun newInstance(searchTerm: String, apiKey: String): SearchFeedFragment {
            val args = Bundle()
            args.putString(SEARCH_TERM_KEY, searchTerm)
            args.putString(API_KEY_KEY, apiKey)
            val fragment = SearchFeedFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getFeedObservable(page : Int): Single<Feed> {
        calling = true
        return RetrofitClient.getNewsInstance()
                .create(NewsApi::class.java)
                .getEverything(
                        searchTerm = searchTerm,
                        apiKey = apiKey,
                        page = page)
    }

    override fun unpackBundle() {
        arguments?.run {
            searchTerm = getString(SEARCH_TERM_KEY)!!
            apiKey = getString(API_KEY_KEY)!!
        }
    }
}