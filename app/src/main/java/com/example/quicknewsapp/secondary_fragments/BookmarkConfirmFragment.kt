package com.example.quicknewsapp.secondary_fragments

import android.os.Bundle
import com.example.quicknewsapp.R
import com.example.quicknewsapp.models.Article
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class BookmarkConfirmFragment : ConfirmFragment() {

    override var confirmLabelStringId: Int = 0
        get() {
            return if (isArticleBookmarked) {
                R.string.confirmation_unbookmark_label
            } else {
                R.string.confirmation_bookmark_label
            }
        }

    override var confirmButtonStringId: Int = 0
        get() {
            return if (isArticleBookmarked) {
                R.string.confirmation_unbookmark_button_text
            } else {
                R.string.confirmation_bookmark_button_text
            }
        }

    companion object {
        fun newInstance(article: Article): ConfirmFragment {
            val fragment = BookmarkConfirmFragment()
            val args = Bundle()
            args.putParcelable(ARTICLE_KEY, article)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onConfirmClicked() {
        val action =
                if (isArticleBookmarked) {
                    article.prepareForDelete()
                    viewModel.delete(article)
                } else {
                    article.prepareForInsert()
                    viewModel.insert(article)
                }

        val dis = action.subscribeOn(Schedulers.computation())
                .subscribe({
                    Timber.e("onConfirmClicked()")
                }, {
                    Timber.e(it)
                })
        compositeDisposable.add(dis)
    }
}