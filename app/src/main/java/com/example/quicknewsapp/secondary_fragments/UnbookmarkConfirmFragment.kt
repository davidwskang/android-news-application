package com.example.quicknewsapp.secondary_fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.quicknewsapp.BookmarkedArticlesViewModel
import com.example.quicknewsapp.R
import com.example.quicknewsapp.models.Article
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class UnbookmarkConfirmFragment : ConfirmFragment() {

    override val confirmLabelStringId = R.string.confirmation_unbookmark_label

    override val confirmButtonStringId = R.string.confirmation_unbookmark_button_text

    lateinit var viewModel : BookmarkedArticlesViewModel

    companion object {
        fun newInstance(article: Article): ConfirmFragment {
            val fragment = UnbookmarkConfirmFragment()
            val args = Bundle()
            args.putParcelable(ARTICLE_KEY, article)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BookmarkedArticlesViewModel::class.java)
    }

    override fun onConfirmClicked() {
        val disposable = viewModel.delete(article)
                .subscribeOn(Schedulers.computation())
                .subscribe({
                    Timber.e("onConfirmClicked()")
                }, {
                    Timber.e(it)
                })
        compositeDisposable.add(disposable)
    }
}