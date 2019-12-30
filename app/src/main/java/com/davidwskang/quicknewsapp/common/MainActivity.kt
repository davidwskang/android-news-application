package com.davidwskang.quicknewsapp.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.davidwskang.quicknewsapp.R
import com.davidwskang.quicknewsapp.secondary_fragments.about.AboutFragment
import com.davidwskang.quicknewsapp.main_fragments.AppMainFragment
import com.davidwskang.quicknewsapp.main_fragments.bookmarks.BookmarksFragment
import com.davidwskang.quicknewsapp.secondary_fragments.ArticleFragment
import com.davidwskang.quicknewsapp.main_fragments.headlines.HeadlinesFragment
import com.davidwskang.quicknewsapp.models.LocationInformation
import com.davidwskang.quicknewsapp.models.Source
import com.davidwskang.quicknewsapp.main_fragments.search.view.SearchFragment
import com.davidwskang.quicknewsapp.models.Article
import com.davidwskang.quicknewsapp.secondary_fragments.ConfirmFragment
import com.davidwskang.quicknewsapp.secondary_fragments.settings.SettingsFragment
import com.davidwskang.quicknewsapp.secondary_fragments.splash.SplashScreenFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main_layout.*
import timber.log.Timber


class MainActivity : AppCompatActivity(),
        AppActionBar.ActionBarClickListener {

    lateinit var headlinesFragment      : HeadlinesFragment
    lateinit var searchFragment         : SearchFragment
    lateinit var bookmarksFragment      : BookmarksFragment
    lateinit var splashFragment         : SplashScreenFragment
    lateinit var currFragment           : AppMainFragment

    var screenHeight    : Int = 0
    var screenWidth     : Int = 0

    var sources : HashSet<Source> = HashSet()
    var location : LocationInformation? = null
    lateinit var countryCode : String

    lateinit var API_KEY : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_layout)
        Timber.plant(Timber.DebugTree())

        splashFragment = SplashScreenFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .add(R.id.full_screen_fragment_container, splashFragment)
                .show(splashFragment)
                .commit()

        searchFragment = SearchFragment.newInstance()
        headlinesFragment = HeadlinesFragment.newInstance()
        bookmarksFragment = BookmarksFragment.newInstance()

        supportFragmentManager.beginTransaction()
                .add(R.id.main_fragment_container, bookmarksFragment)
                .hide(bookmarksFragment)
                .add(R.id.main_fragment_container, searchFragment)
                .hide(searchFragment)
                .add(R.id.main_fragment_container, headlinesFragment)
                .hide(headlinesFragment)
                .commit()

        action_bar.setActionBarClickListener(this)
        bottom_navigation.setOnNavigationItemSelectedListener(getBottomNavListener())
        bottom_drawer_layout.setBottomDrawerMenuSelectedListener(getBottomDrawerListener())

        currFragment = headlinesFragment
        action_bar.updateActionBar(currFragment)
        bottom_navigation.menu.getItem(currFragment.fragmentIndex)?.isChecked = true
    }

    override fun onLeftIconActionBarClicked() {
        if (currFragment.fragmentIndex == Constants.SEARCH_INDEX) {
            (currFragment as SearchFragment).showSearchHistory()
        }
    }

    override fun onRightIconActionBarClicked() {
        bottom_drawer_layout.openDrawer()
    }

    override fun onSearchByActionBar(searchTerm: String) {
        if (currFragment.fragmentIndex == Constants.SEARCH_INDEX) {
            (currFragment as SearchFragment).doSearch(searchTerm)
        }
    }

    fun updateActionBar(fragment : AppMainFragment) {
        action_bar.updateActionBar(fragment)
    }

    fun getAppBar() : AppActionBar = action_bar

    fun onLocationReceived(location : LocationInformation) {
        countryCode = location.countryCode
        this.location = location
    }

    fun destroySplashScreen() {
        supportFragmentManager.beginTransaction()
                .remove(splashFragment)
                .show(headlinesFragment)
                .commit()
    }

    fun onGoToArticleConfirmed(article : Article) {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.anim_in_bot_to_top, R.anim.anim_out_top_to_bot)
                .add(R.id.full_screen_fragment_container, ArticleFragment.newInstance(article))
                .commit()
    }

    fun showConfirmModal(confirmFrag : ConfirmFragment) {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.full_screen_fragment_container, confirmFrag)
                .commit()
    }

    private fun getBottomDrawerListener() : BottomDrawerMenuContainer.BottomDrawerMenuItemSelectedListener {
        return object : BottomDrawerMenuContainer.BottomDrawerMenuItemSelectedListener {
            override fun onBottomDrawerMenuItemSelected(position: Int) {
                bottom_drawer_layout.closeDrawer()

                val nextFrag =
                        if (position == 0) {
                            SettingsFragment.newInstance(location!!)
                        } else {
                            AboutFragment.newInstance()
                        }

                supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.anim_in_left_to_right,
                                R.anim.anim_out_left_to_right)
                        .add(R.id.full_screen_fragment_container, nextFrag)
                        .commit()
            }
        }
    }

    private fun getBottomNavListener() : BottomNavigationView.OnNavigationItemSelectedListener {
        return BottomNavigationView.OnNavigationItemSelectedListener {
            val nextFrag = when (it.itemId) {
                R.id.bottom_menu_search -> searchFragment
                R.id.bottom_menu_headlines -> headlinesFragment
                else /* R.id.bottom_menu_bookmarks */   -> {
                    bookmarksFragment
                }
            }
            supportFragmentManager.beginTransaction()
                    .hide(currFragment)
                    .show(nextFrag)
                    .commit()

            bottom_navigation.menu[nextFrag.fragmentIndex].isChecked = true
            action_bar.updateActionBar(nextFrag)
            currFragment = nextFrag
            true
        }
    }

}
