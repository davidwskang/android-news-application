package com.davidwskang.quicknewsapp.view.fragment.main_fragments.bookmarks

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidwskang.quicknewsapp.R
import com.davidwskang.quicknewsapp.model.Article
import com.davidwskang.quicknewsapp.model.Constants
import com.davidwskang.quicknewsapp.service.BookmarkedArticlesViewModel
import com.davidwskang.quicknewsapp.view.fragment.main_fragments.AppMainFragment
import com.davidwskang.quicknewsapp.view.fragment.main_fragments.feed.FeedAdapter
import com.davidwskang.quicknewsapp.view.fragment.secondary_fragments.confirm.BookmarkConfirmFragment
import com.davidwskang.quicknewsapp.view.fragment.secondary_fragments.confirm.OpenArticleConfirmFragment
import kotlinx.android.synthetic.main.fragment_bookmarks.*

class BookmarksFragment : AppMainFragment(), FeedAdapter.OnFeedItemClickedListener {

    override val fragmentIndex = Constants.BOOKMARKS_INDEX
    override val layoutId = R.layout.fragment_bookmarks
    override val actionBarTitle = Constants.BOOKMARKS_TITLE
    override val actionBarIconId = R.drawable.ic_bookmark_border_black_35dp
    override val actionBarSearchVisibility = View.GONE

    lateinit var viewModel: BookmarkedArticlesViewModel

    lateinit var feedAdapter: FeedAdapter

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
        feedAdapter = FeedAdapter(listener = this)

        feedAdapter.loadingItemVisible = false
        bookmarked_articles_list.adapter = feedAdapter
        bookmarked_articles_list.layoutManager = LinearLayoutManager(context)
    }

    override fun onFeedItemClicked(article: Article) {
        mainActivity!!.showConfirmModal(OpenArticleConfirmFragment.newInstance(article))
    }

    override fun onFeedItemLongPress(article: Article) {
        mainActivity!!.showConfirmModal(BookmarkConfirmFragment.newInstance(article))
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