package com.davidwskang.quicknewsapp.main_fragments.search.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.davidwskang.quicknewsapp.R
import com.davidwskang.quicknewsapp.main_fragments.search.SearchItem
import kotlinx.android.synthetic.main.item_search.view.*
import kotlinx.android.synthetic.main.item_search_clear_all.view.*
import java.util.*

class SearchHistoryAdapter(var searchListener: OnRecentSearchClickedListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val searchHistory = ArrayList<SearchItem>()

    companion object {
        private const val ITEM_SEARCH = 0
        private const val ITEM_CLEAR_SEARCHES = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == ITEM_SEARCH) {
            v = inflater.inflate(R.layout.item_search, parent, false)
            RecentSearchViewHolder(v)
        } else {
            v = inflater.inflate(R.layout.item_search_clear_all, parent, false)
            RecentSearchClearAllViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        if (type == ITEM_SEARCH) {
            (holder as RecentSearchViewHolder).bind(searchHistory[position])
        } else {
            (holder as RecentSearchClearAllViewHolder).bind()
        }
    }

    override fun getItemCount(): Int {
        return if (searchHistory.isEmpty()) {
            0
        } else searchHistory.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < searchHistory.size) {
            ITEM_SEARCH // regular
        } else {
            ITEM_CLEAR_SEARCHES // footer ("clear all")
        }
    }

    fun setRecentSearchItems(searchItems: ArrayList<SearchItem>) {
        searchHistory.clear()
        searchHistory.addAll(searchItems)
        notifyDataSetChanged()
    }

    fun emptySearchHistory() {
        searchHistory.clear()
        notifyDataSetChanged()
    }

    internal inner class RecentSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(searchItem: SearchItem) = itemView.run {
                recent_search_text.text = searchItem.searchTerm
                recent_search_text.setOnClickListener {
                    v: View? -> searchListener.onSearchItemClicked(searchItem)
                }
                close_button.setOnClickListener { v: View? ->
                    searchHistory.remove(searchItem)
                    notifyItemRemoved(adapterPosition)

                    searchListener.onSearchItemRemoveClicked(searchItem)
                    if (searchHistory.isEmpty()) {
                        notifyItemRemoved(1)
                    }
                }
        }

    }

    internal inner class RecentSearchClearAllViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() = itemView.clear_all_searches_text.setOnClickListener {
            searchListener.onClearHistoryClicked()
        }
    }

    interface OnRecentSearchClickedListener {

        fun onSearchItemClicked(searchItem: SearchItem)

        fun onSearchItemRemoveClicked(searchItem : SearchItem)

        fun onClearHistoryClicked()
    }
}