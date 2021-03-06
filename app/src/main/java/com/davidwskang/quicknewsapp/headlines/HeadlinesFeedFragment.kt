package com.davidwskang.quicknewsapp.headlines

import android.os.Bundle
import com.davidwskang.quicknewsapp.feed.FeedFragment
import com.davidwskang.quicknewsapp.service.NewsApi
import com.davidwskang.quicknewsapp.service.RetrofitClient
import com.davidwskang.quicknewsapp.model.Feed
import io.reactivex.Single

class HeadlinesFeedFragment : FeedFragment() {

    lateinit var countryCode: String
    lateinit var category: String

    companion object {
        @JvmStatic
        fun newInstance(countryCode: String, category: String, apiKey: String): FeedFragment {
            val args = Bundle()
            args.putString(COUNTRY_CODE_KEY, countryCode)
            args.putString(CATEGORY_KEY, category)
            args.putString(API_KEY_KEY, apiKey)
            val fragment = HeadlinesFeedFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun unpackBundle() {
        arguments?.run {
            countryCode = getString(COUNTRY_CODE_KEY)!!
            category = getString(CATEGORY_KEY)!!
            apiKey = getString(API_KEY_KEY)!!
        }
    }

    override fun getFeedObservable(page : Int): Single<Feed> {
        calling = true
        return RetrofitClient.getNewsInstance()
                .create(NewsApi::class.java)
                .getTopHeadlines(
                        country = countryCode,
                        category = category,
                        apiKey = apiKey,
                        page = page)
    }
}