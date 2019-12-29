package com.example.quicknewsapp.main_fragments.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.quicknewsapp.BookmarkedArticlesViewModel
import com.example.quicknewsapp.main_fragments.AppMainFragment
import com.example.quicknewsapp.common.Constants
import com.example.quicknewsapp.R
import com.example.quicknewsapp.main_fragments.feed.FeedAdapter
import com.example.quicknewsapp.models.Article
import com.example.quicknewsapp.main_fragments.preview.PreviewFragment
import com.example.quicknewsapp.util.AppUtils
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

    lateinit var viewModel : BookmarkedArticlesViewModel

    lateinit var feedAdapter : FeedAdapter

    companion object {
        fun newInstance(): BookmarksFragment {
            val args = Bundle()
            val fragment = BookmarksFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BookmarkedArticlesViewModel::class.java)
        viewModel.getAll().observe(
                this,
                Observer<List<Article>> {
                    feedAdapter.setFeedArticles(it as ArrayList<Article>)
                    feedAdapter.setBookmarkedArticles(it)
                    updateView()
                })

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

//        AppUtils.getBookmarksDao(mainActivity!!).getAllArticles().observe(
//                this, Observer<List<Article>> {
//            feedAdapter.setFeedArticles(it as ArrayList<Article>)
//            feedAdapter.setBookmarkedArticles(it)
//            updateView()
//        })
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