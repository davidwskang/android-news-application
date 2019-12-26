package com.example.quicknewsapp.secondary_fragments

import android.os.Bundle
import com.example.quicknewsapp.R
import com.example.quicknewsapp.models.Article
import com.example.quicknewsapp.util.AppUtils
import io.reactivex.CompletableSource
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class UnbookmarkConfirmFragment : ConfirmFragment() {

    override val confirmLabelStringId = R.string.confirmation_unbookmark_label

    override val confirmButtonStringId = R.string.confirmation_unbookmark_button_text

    companion object {
        fun newInstance(article: Article): ConfirmFragment {
            val fragment = UnbookmarkConfirmFragment()
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
                    .deleteArticle(article)
                    .subscribeOn(Schedulers.computation())
                    .doOnError { Timber.e(it) }
        }
    }
}