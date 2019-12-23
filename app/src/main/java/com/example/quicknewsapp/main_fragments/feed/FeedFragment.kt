package com.example.quicknewsapp.main_fragments.feed

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.quicknewsapp.R
import com.example.quicknewsapp.common.AppFragment
import com.example.quicknewsapp.common.Constants
import com.example.quicknewsapp.models.Article
import com.example.quicknewsapp.models.Feed
import com.example.quicknewsapp.main_fragments.bookmarks.BookmarkedArticlesDatabase
import com.example.quicknewsapp.main_fragments.feed.FeedAdapter.OnFeedItemClickedListener
import com.example.quicknewsapp.main_fragments.preview.PreviewFragment
import com.example.quicknewsapp.util.AppUtils
import io.reactivex.Single
import io.reactivex.SingleObserver
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
    var feedAdapter: FeedAdapter? = null

    enum class FeedCallType {
        INITIALIZE, REFRESH, PAGINATION
    }

    companion object {
        const val SEARCH_TERM_KEY = "search_term_key"
        const val API_KEY_KEY = "api_key_key"
        const val COUNTRY_CODE_KEY = "country_code_key"
        const val CATEGORY_KEY = "category_key"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBundleValues()

        initRecyclerView()

        swipe_refresh_layout.setOnRefreshListener { getFeed(FeedCallType.REFRESH) }

        getFeed(FeedCallType.INITIALIZE)
    }

    override fun onFeedItemClicked(article: Article) {
        checkIfSaved(article)
    }

    abstract fun getFeedObservable(page : Int) : Single<Feed>

    abstract fun initBundleValues()

    private fun initRecyclerView() {
        val imageHeight = AppUtils.convertDipToPx(context!!, 240f)
        feedAdapter = FeedAdapter(
                listener = this,
                imageHeight = imageHeight,
                imageWidth = mainActivity!!.screenWidth)
        results_list.apply {
            adapter = feedAdapter
            layoutManager = object : LinearLayoutManager(context) {
                override fun onScrollStateChanged(state: Int) {
                    super.onScrollStateChanged(state)
                    if (findLastVisibleItemPosition() == feedAdapter?.itemCount?.minus(1)
                            && feedAdapter!!.loadingItemVisible) {
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

    private fun showPreviewFragment(article: Article) {
        mainActivity!!.supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.anim_in_bot_to_top, R.anim.anim_out_top_to_bot)
                .add(R.id.full_screen_fragment_container, PreviewFragment.newInstance(article))
                .commit()
    }

    private fun checkIfSaved(article: Article) {
        val observable = object : Single<Article>() {
            override fun subscribeActual(observer: SingleObserver<in Article>) {
                try {
                    val db = Room.databaseBuilder(activity!!.applicationContext,
                            BookmarkedArticlesDatabase::class.java, Constants.SAVED_DB).build()
                    val savedArticle = db.savedArticlesDao().getArticleByTitle(article.title)
                    if (savedArticle == null) {
                        observer.onSuccess(article)
                    } else {
                        observer.onSuccess(savedArticle)
                    }
                } catch (e: Exception) {
                    observer.onError(e)
                }
            }
        }
        val observer = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe({
                    showPreviewFragment(it)
                }, {
                    Timber.e(it)
                })

        compositeDisposable.add(observer)
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
                    feedAdapter!!.setFeedArticles(articles)
                    feedAdapter!!.loadingItemVisible = true
                }
                FeedCallType.REFRESH -> {
                    feedAdapter!!.setFeedArticles(articles)
                    feedAdapter!!.loadingItemVisible = true
                    this.swipe_refresh_layout.isRefreshing = false
                }
                FeedCallType.PAGINATION -> {
                    feedAdapter!!.addArticles(articles)
                    if (articles.isEmpty()) {
                        feedAdapter!!.loadingItemVisible = false
                    }
                }
            }
        }
    }
}
