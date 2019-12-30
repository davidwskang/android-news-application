package com.example.quicknewsapp.main_fragments.feed

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quicknewsapp.BookmarkedArticlesViewModel
import com.example.quicknewsapp.R
import com.example.quicknewsapp.common.AppFragment
import com.example.quicknewsapp.main_fragments.feed.FeedAdapter.OnFeedItemClickedListener
import com.example.quicknewsapp.models.Article
import com.example.quicknewsapp.models.Feed
import com.example.quicknewsapp.secondary_fragments.BookmarkConfirmFragment
import com.example.quicknewsapp.secondary_fragments.OpenArticleConfirmFragment
import com.example.quicknewsapp.util.AppUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_results.*
import timber.log.Timber
import kotlin.reflect.full.memberProperties

abstract class FeedFragment : AppFragment(), OnFeedItemClickedListener {

    override val layoutId = R.layout.fragment_results

    var calling: Boolean = false
    var page: Int = 1
    lateinit var apiKey: String

    lateinit var feedAdapter: FeedAdapter
    lateinit var viewModel: BookmarkedArticlesViewModel

    enum class FeedCallType {
        INITIALIZE, REFRESH, PAGINATION
    }

    companion object {
        const val SEARCH_TERM_KEY = "search_term_key"
        const val API_KEY_KEY = "api_key_key"
        const val COUNTRY_CODE_KEY = "country_code_key"
        const val CATEGORY_KEY = "category_key"
    }

    abstract fun getFeedObservable(page: Int): Single<Feed>

    abstract fun unpackBundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this)
                .get(BookmarkedArticlesViewModel::class.java)
        viewModel.getAll().observe(
                this,
                Observer<List<Article>> {
                    feedAdapter.setBookmarkedArticles(it)
                })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        unpackBundle()

        initRecyclerView()

        getFeed(FeedCallType.INITIALIZE)
    }

    override fun onResume() {
        super.onResume()
        Timber.e("onResume()")
    }

    override fun onFeedItemClicked(article: Article) {
        mainActivity!!.supportFragmentManager
                .beginTransaction()
                .add(R.id.full_screen_fragment_container, OpenArticleConfirmFragment.newInstance(article))
                .commit()
    }

    override fun onFeedItemLongPress(article: Article) {
        mainActivity!!.showConfirmModal(BookmarkConfirmFragment.newInstance(article))
    }

    private fun initRecyclerView() {
        feedAdapter = FeedAdapter(listener = this)
        results_list.apply {
            adapter = feedAdapter
            layoutManager = object : LinearLayoutManager(context) {
                override fun onScrollStateChanged(state: Int) {
                    super.onScrollStateChanged(state)
                    if (findLastVisibleItemPosition() == feedAdapter.itemCount.minus(1)
                            && feedAdapter.loadingItemVisible) {
                        getFeed(FeedCallType.PAGINATION)
                    }
                }
            }
        }
        swipe_refresh_layout.setOnRefreshListener { getFeed(FeedCallType.REFRESH) }
    }

    private fun getFeed(feedCallType: FeedCallType) {
        if (calling) return
        if (feedCallType == FeedCallType.PAGINATION) {
            page += 1
        } else {
            page = 1
        }
        val disposable = getFeedObservable(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(cleanArticles())
                .map(filterArticles())
                .subscribe(
                        onSuccess(feedCallType),
                        Consumer {
                            calling = false
                            Timber.e(it)
                        })

        compositeDisposable.add(disposable)
    }

    private fun cleanArticles(): Function<Feed, Feed> {
        return Function {
            val cleanedArticles = ArrayList<Article>()
            for (article in it.articles) {
                article.apply {
                    val regexEscapeChars = Regex("""(\\r)?\\n""")
                    val newContent = regexEscapeChars.replace(this.content!!, " ")
                    val regexContentEnding = Regex("""\[\+\d+\schars]""")
                    this.content = regexContentEnding.replace(newContent, "")
                    this.publishedDate = AppUtils.getDate(this.publishedDate!!)
                }
                cleanedArticles.add(article)
            }
            it.articles = cleanedArticles
            return@Function it
        }
    }

    private fun filterArticles(): Function<Feed, Feed> {
        return Function {
            val filteredArticles = ArrayList<Article>()
            for (article in it.articles) {
                var containsEmptyString = false
                for (prop in Article::class.memberProperties) {
                    if (prop.name != "author" && prop.get(article) is String
                            && (prop.get(article) as String).isEmpty()) {
                        containsEmptyString = true
                        break
                    }
                }
                if (!containsEmptyString) {
                    filteredArticles.add(article)
                }
            }
            it.articles = filteredArticles
            return@Function it
        }
    }

    private fun onSuccess(feedCallType: FeedCallType): Consumer<Feed> {
        return Consumer {
            val articles = ArrayList<Article>(it.articles)
            when (feedCallType) {
                FeedCallType.INITIALIZE -> {
                    feedAdapter.setFeedArticles(articles)
                    feedAdapter.loadingItemVisible = true
                }
                FeedCallType.REFRESH -> {
                    feedAdapter.setFeedArticles(articles)
                    feedAdapter.loadingItemVisible = true
                    swipe_refresh_layout.isRefreshing = false
                }
                FeedCallType.PAGINATION -> {
                    feedAdapter.addArticles(articles)
                    if (articles.isEmpty()) {
                        feedAdapter.loadingItemVisible = false
                    }
                }
            }
            calling = false
        }
    }
}
