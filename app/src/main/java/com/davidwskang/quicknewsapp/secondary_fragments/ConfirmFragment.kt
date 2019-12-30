package com.davidwskang.quicknewsapp.secondary_fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.davidwskang.quicknewsapp.BookmarkedArticlesViewModel
import com.davidwskang.quicknewsapp.R
import com.davidwskang.quicknewsapp.common.AppFragment
import com.davidwskang.quicknewsapp.models.Article
import kotlinx.android.synthetic.main.fragment_confirm.*

abstract class ConfirmFragment : AppFragment() {

    override val layoutId = R.layout.fragment_confirm

    abstract var confirmLabelStringId: Int

    abstract var confirmButtonStringId: Int

    lateinit var article: Article

    lateinit var viewModel: BookmarkedArticlesViewModel

    var isArticleBookmarked = false

    companion object {
        const val ARTICLE_KEY = "article_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        unpackBundle()

        viewModel = ViewModelProviders.of(this).get(BookmarkedArticlesViewModel::class.java)

        viewModel.getArticleByTitle(article.title).observe(this, Observer {
            isArticleBookmarked = it != null
            updateView(article)
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
    }

    private fun unpackBundle() {
        arguments?.run {
            article = getParcelable(ARTICLE_KEY)!!
        }
    }

    private fun updateView(article: Article) {
        confirmation_title.text = article.title
        resources.run {
            confirmation_label.text = getString(confirmLabelStringId)
            confirm_button.text = getString(confirmButtonStringId)
            cancel_button.text = getString(R.string.confirmation_cancel_button_text)
        }
    }

    private fun initButtons() {
        confirm_button.setOnClickListener {
            onConfirmClicked()
            removeConfirmationFragment()
        }
        cancel_button.setOnClickListener {
            removeConfirmationFragment()
        }
        root.setOnClickListener {
            removeConfirmationFragment()
        }
    }

    abstract fun onConfirmClicked()

    fun removeConfirmationFragment() {
        mainActivity!!.supportFragmentManager
                .beginTransaction()
                .remove(this)
                .commit()
    }
}