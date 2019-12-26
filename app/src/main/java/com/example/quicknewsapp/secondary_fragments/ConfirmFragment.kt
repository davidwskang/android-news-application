package com.example.quicknewsapp.secondary_fragments

import android.os.Bundle
import android.view.View
import com.example.quicknewsapp.R
import com.example.quicknewsapp.common.AppFragment
import com.example.quicknewsapp.models.Article
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.fragment_confirm.*
import timber.log.Timber

abstract class ConfirmFragment : AppFragment() {

    override val layoutId = R.layout.fragment_confirm

    abstract val confirmLabelStringId : Int

    abstract val confirmButtonStringId : Int

    lateinit var article: Article

    companion object {
        const val ARTICLE_KEY = "article_key"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unpackBundle()
        updateView(article)
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
        val confirmButton = RxView.clicks(confirm_button)
                .observeOn(AndroidSchedulers.mainThread())
                .switchMapCompletable(onConfirmCompletable())
                .subscribe()

        val cancelButton = RxView.clicks(cancel_button)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    removeConfirmationFragment()
                },{
                    Timber.e(it)
                })

        val rootButton = RxView.clicks(root)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    removeConfirmationFragment()
                },{
                    Timber.e(it)
                })

        compositeDisposable.addAll(confirmButton, cancelButton, rootButton)
    }

    abstract fun onConfirmCompletable(): Function<in Any?, out CompletableSource>

    protected fun removeConfirmationFragment() {
        mainActivity!!.supportFragmentManager
                .beginTransaction()
                .remove(this)
                .commit()
    }
}