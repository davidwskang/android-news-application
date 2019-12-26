package com.example.quicknewsapp.secondary_fragments

import android.os.Bundle
import com.example.quicknewsapp.R
import com.example.quicknewsapp.models.Article
import com.example.quicknewsapp.util.AppUtils
import io.reactivex.CompletableSource
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class BookmarkConfirmFragment : ConfirmFragment() {

    override val confirmLabelStringId = R.string.confirmation_bookmark_label

    override val confirmButtonStringId = R.string.confirmation_bookmark_button_text

    companion object {
        fun newInstance(article: Article): ConfirmFragment {
            val fragment = BookmarkConfirmFragment()
            val args = Bundle()
            args.putParcelable(ARTICLE_KEY, article)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onConfirmCompletable(): Function<in Any?, out CompletableSource> {
        return Function {
            article.prepareForInsert()
            AppUtils.getBookmarksDao(mainActivity!!)
                    .insertArticle(article)
                    .subscribeOn(Schedulers.computation())
                    .doOnComplete{ removeConfirmationFragment() }
                    .doOnError { Timber.e(it) }
        }
    }
}