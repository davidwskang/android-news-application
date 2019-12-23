package com.example.quicknewsapp.main_fragments.headlines

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.quicknewsapp.main_fragments.AppMainFragment
import com.example.quicknewsapp.common.Constants
import com.example.quicknewsapp.R
import com.example.quicknewsapp.main_fragments.feed.FeedFragment
import com.example.quicknewsapp.main_fragments.feed.HeadlinesFeedFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_headlines.*

class HeadlinesFragment : AppMainFragment() {

    override val layoutId                   = R.layout.fragment_headlines
    override val fragmentIndex              = Constants.HEADLINES_INDEX
    override val actionBarTitle             = Constants.HEADLINES_TITLE
    override val actionBarIconId            = R.drawable.ic_trending_up_black_35dp
    override val actionBarSearchVisibility  = View.GONE

    companion object {
        fun newInstance(): HeadlinesFragment {
            val args = Bundle()
            val fragment = HeadlinesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        headlines_pager.adapter = HeadlinesAdapter(this)

        TabLayoutMediator(tab_layout, headlines_pager) { tab, position ->
            tab.text = Constants.CATEGORIES[position]
        }.attach()

    }

    inner class HeadlinesAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = Constants.CATEGORIES.size

        override fun createFragment(position: Int): Fragment {
            return HeadlinesFeedFragment.newInstance(
                    category = Constants.CATEGORIES[position],
                    countryCode = Constants.COUNTRY_CODES[7], // canada by default
                    apiKey = mainActivity!!.API_KEY)
        }
    }
}