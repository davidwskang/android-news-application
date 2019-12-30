package com.davidwskang.quicknewsapp.view.fragment.secondary_fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.davidwskang.quicknewsapp.R
import com.davidwskang.quicknewsapp.view.fragment.AppFragment
import com.davidwskang.quicknewsapp.model.Article
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_article.*
import timber.log.Timber

class ArticleFragment : AppFragment() {

    lateinit var article: Article
    override val layoutId = R.layout.fragment_article

    companion object {

        private const val ARTICLE_KEY = "article_key"

        fun newInstance(article: Article): ArticleFragment {
            val args = Bundle()
            args.putParcelable(ARTICLE_KEY, article)
            val fragment = ArticleFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.run {
            article = getParcelable(ARTICLE_KEY)!!
            web_view.loadUrl(article.articleUrl)
            Timber.e("article url: ${article.articleUrl}")
            setBookmarkImageStatus(article.isBookmarked)
            article_url.text = article.articleUrl

            web_view.webViewClient = object : WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    view?.visibility = View.VISIBLE
                    article_progress_bar?.visibility = View.GONE
                }
            }
        }

        val backButton = RxView.clicks(article_back_button)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mainActivity!!.supportFragmentManager
                            .beginTransaction()
                            .remove(this)
                            .commit()
                }, {
                    Timber.e(it)
                })

        compositeDisposable.addAll(backButton)
    }

    private fun setBookmarkImageStatus(bookmarked: Boolean) {
        if (bookmarked) {
            article_bookmark_button.setImageResource(R.drawable.ic_bookmark_black_35dp)
        } else {
            article_bookmark_button.setImageResource(R.drawable.ic_bookmark_border_black_35dp)
        }
    }
}