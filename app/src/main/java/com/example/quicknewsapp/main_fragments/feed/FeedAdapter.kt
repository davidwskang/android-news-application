package com.example.quicknewsapp.main_fragments.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quicknewsapp.R
import com.example.quicknewsapp.models.Article
import com.example.quicknewsapp.util.AppUtils
import com.jakewharton.rxbinding2.view.RxView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.item_feed_block.view.*
import kotlinx.android.synthetic.main.item_feed_regular.view.*

class FeedAdapter(var listener: OnFeedItemClickedListener,
                  var imageHeight: Int,
                  var imageWidth: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var articles = ArrayList<Article>()

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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View
        return if (viewType == ITEM) {
            view = inflater.inflate(R.layout.item_feed_regular, parent, false)
            RegularFeedViewHolder(view)
        } else {
            view = inflater.inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RegularFeedViewHolder) {
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

    fun addArticles(newArticles: ArrayList<Article>) {
        articles.addAll(newArticles as Collection<Article>)
        notifyDataSetChanged()
    }

    fun setFeedArticles(newArticles: ArrayList<Article>) {
        articles.clear()
        articles.addAll(newArticles as Collection<Article>)
        notifyDataSetChanged()
    }

    internal inner class BlockFeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(article: Article) {
            itemView.run {
                Picasso.get()
                        .load(article.imageUrl)
                        .resize(imageWidth, imageHeight)
                        .centerCrop()
                        .into(image)
                title.text = article.title
                source_name.text = article.source?.name
                setOnClickListener { listener.onFeedItemClicked(article) }
            }
        }
    }

    internal inner class RegularFeedViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(article : Article) {
            itemView.run {
                article_date.text = article.publishedDate
                val length = AppUtils.convertDipToPx(itemView.context, 80f)
                Picasso.get()
                        .load(article.imageUrl)
                        .resize(length, length)
                        .centerCrop()
                        .into(article_image)
                article_source.text = article.source?.name
                article_title.text = article.title

                RxView.clicks(itemView)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { listener.onFeedItemClicked(article) }
            }
        }
    }

    internal inner class LoadingViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)

    interface OnFeedItemClickedListener {
        fun onFeedItemClicked(article: Article)
    }

}