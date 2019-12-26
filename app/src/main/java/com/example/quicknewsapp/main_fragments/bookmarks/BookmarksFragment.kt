package com.example.quicknewsapp.main_fragments.bookmarks

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.quicknewsapp.main_fragments.AppMainFragment
import com.example.quicknewsapp.common.Constants
import com.example.quicknewsapp.R
import com.example.quicknewsapp.main_fragments.feed.FeedAdapter
import com.example.quicknewsapp.models.Article
import com.example.quicknewsapp.main_fragments.preview.PreviewFragment
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_bookmarks.*
import timber.log.Timber

class BookmarksFragment : AppMainFragment() , FeedAdapter.OnFeedItemClickedListener {

    override val fragmentIndex              = Constants.BOOKMARKS_INDEX
    override val layoutId                   = R.layout.fragment_bookmarks
    override val actionBarTitle             = Constants.BOOKMARKS_TITLE
    override val actionBarIconId            = R.drawable.ic_bookmark_border_black_35dp
    override val actionBarSearchVisibility  = View.GONE

    lateinit var feedAdapter : FeedAdapter

    companion object {
        fun newInstance(): BookmarksFragment {
            val args = Bundle()
            val fragment = BookmarksFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedAdapter = FeedAdapter(
                listener = this,
                imageWidth = mainActivity!!.screenWidth,
                imageHeight = mainActivity!!.screenHeight)

        feedAdapter.loadingItemVisible = false
        bookmarked_articles_list.adapter = feedAdapter
        bookmarked_articles_list.layoutManager = LinearLayoutManager(context)
        loadBookmarkedArticles()
    }

    override fun onFeedItemClicked(article: Article) {
        mainActivity!!.supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.anim_in_bot_to_top, R.anim.anim_out_top_to_bot)
                .add(R.id.full_screen_fragment_container, PreviewFragment.newInstance(article))
                .commit()
    }

    override fun onFeedItemLongPress(article: Article) {
        Timber.e("item was long pressed")
    }

    fun loadBookmarkedArticles() {
        val observable = object : Single<ArrayList<Article>>() {
            override fun subscribeActual(observer: SingleObserver<in ArrayList<Article>>) {
                try {
                    val db = Room.databaseBuilder(activity!!.applicationContext,
                            BookmarkedArticlesDatabase::class.java, Constants.SAVED_DB).build()
                    val savedArticles = db.savedArticlesDao().getAll() as ArrayList
                    observer.onSuccess(savedArticles)
                } catch (e : Exception) {
                    observer.onError(e)
                }
            }
        }
        val observer = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe({
                    feedAdapter.setFeedArticles(it)
                    updateView()
                }, {
                    Timber.e(it)
                })
        compositeDisposable.add(observer)
    }

    private fun updateView() {
        if (feedAdapter.itemCount == 0) {
            bookmarks_default_message.visibility = View.VISIBLE
            bookmarked_articles_list.visibility = View.GONE
        } else {
            bookmarks_default_message.visibility = View.GONE
            bookmarked_articles_list.visibility = View.VISIBLE
        }
    }
}