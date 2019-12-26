package com.example.quicknewsapp.secondary_fragments

import android.os.Bundle
import android.view.View
import com.example.quicknewsapp.R
import com.example.quicknewsapp.common.AppFragment
import com.example.quicknewsapp.models.Article
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_confirmation.*
import timber.log.Timber

class ConfirmationFragment : AppFragment() {

    override val layoutId = R.layout.fragment_confirmation

    lateinit var article: Article

    companion object {

        private const val ARTICLE_KEY = "article_key"

        fun newInstance(article: Article): ConfirmationFragment {
            val fragment = ConfirmationFragment()
            val args = Bundle()
            args.putParcelable(ARTICLE_KEY, article)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        unpackBundle()

        confirmation_title.text = article.title

        val confirmButton = RxView.clicks(confirm_button)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    removeConfirmationFragment()
                    mainActivity!!.onGoToArticleConfirmed(article)
                },{
                    Timber.e(it)
                })
        val cancelButton = RxView.clicks(cancel_button)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    removeConfirmationFragment()
                },{
                    Timber.e(it)
                })
        compositeDisposable.addAll(confirmButton, cancelButton)
    }

    private fun removeConfirmationFragment() {
        mainActivity!!.supportFragmentManager
                .beginTransaction()
                .remove(this)
                .commit()
    }

    private fun unpackBundle() {
        arguments?.run {
            article = getParcelable(ARTICLE_KEY)!!
        }
    }



}