package com.example.quicknewsapp.secondary_fragments

import android.os.Bundle
import com.example.quicknewsapp.R
import com.example.quicknewsapp.models.Article
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.CompletableSource
import io.reactivex.functions.Function

class OpenArticleConfirmFragment : ConfirmFragment() {

    override val confirmLabelStringId = R.string.confirmation_open_label

    override val confirmButtonStringId = R.string.confirmation_open_button_text

    companion object {
        fun newInstance(article: Article): ConfirmFragment {
            val fragment = OpenArticleConfirmFragment()
            val args = Bundle()
            args.putParcelable(ARTICLE_KEY, article)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onConfirmCompletable(): Function<in Any?, out CompletableSource> {
        return Function {
            object : Completable() {
                override fun subscribeActual(observer: CompletableObserver?) {
                    removeConfirmationFragment()
                    mainActivity!!.onGoToArticleConfirmed(article)
                    observer?.onComplete()
                }
            }
        }
    }
}