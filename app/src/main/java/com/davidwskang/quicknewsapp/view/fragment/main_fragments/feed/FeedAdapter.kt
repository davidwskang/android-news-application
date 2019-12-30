package com.davidwskang.quicknewsapp.view.fragment.main_fragments.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.davidwskang.quicknewsapp.R
import com.davidwskang.quicknewsapp.model.Article
import com.davidwskang.quicknewsapp.util.AppUtils
import com.jakewharton.rxbinding2.view.RxView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.item_feed_regular.view.*

class FeedAdapter(var listener: OnFeedItemClickedListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var articles = ArrayList<Article>()
    private var bookmarkedArticles = HashSet<Article>()
    private var itemHeightAndWidthPx = 0 // item is a square

    var loadingItemVisible: Boolean = true
        set(visible) {
            field = visible
            if (visible) {
                notifyItemInserted(articles.size)
            } else {
                notifyItemRemoved(articles.size)
            }
        }

    companion object {
        private const val ITEM = 0
        private const val LOADING_ITEM = 1
        private const val ITEM_HEIGHT_DP = 80f
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View
        return if (viewType == ITEM) {
            view = inflater.inflate(R.layout.item_feed_regular, parent, false)
            FeedViewHolder(view)
        } else {
            view = inflater.inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FeedViewHolder) {
            holder.bind(articles[position])
        }
        // loading view holder does not bind data
    }

    override fun getItemCount(): Int {
        return if (loadingItemVisible) {
            articles.size + 1
        } else {
            articles.size
        }
    }

    override fun getItemViewType(position: Int): Int = if (position < articles.size) ITEM else LOADING_ITEM

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        itemHeightAndWidthPx = AppUtils.convertDipToPx(recyclerView.context, ITEM_HEIGHT_DP)
    }

    fun addArticles(newArticles: ArrayList<Article>) {
        articles.addAll(newArticles as Collection<Article>)
        notifyDataSetChanged()
    }

    fun setFeedArticles(newArticles: ArrayList<Article>) {
        articles.clear()
        articles.addAll(newArticles as Collection<Article>)
        notifyDataSetChanged()
    }

    fun setBookmarkedArticles(bookmarked: List<Article>) {
        bookmarkedArticles.clear()
        bookmarkedArticles.addAll(bookmarked)
        notifyDataSetChanged()
    }

    internal inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(article: Article) {

            itemView.run {
                article_date.text = article.publishedDate
                article_source.text = article.source?.name
                article_title.text = article.title

                Picasso.get()
                        .load(article.imageUrl)
                        .resize(itemHeightAndWidthPx, itemHeightAndWidthPx)
                        .centerCrop()
                        .into(article_image)

                if (bookmarkedArticles.contains(article)) {
                    bookmarked_status.setImageResource(R.drawable.ic_bookmark_black_35dp)
                } else {
                    bookmarked_status.setImageResource(R.drawable.ic_bookmark_border_black_35dp)
                }

                RxView.clicks(this)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { listener.onFeedItemClicked(article) }

                RxView.longClicks(this)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { listener.onFeedItemLongPress(article) }
            }
        }
    }

    internal inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnFeedItemClickedListener {

        fun onFeedItemClicked(article: Article)

        fun onFeedItemLongPress(article: Article)
    }

}