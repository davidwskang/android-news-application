package com.example.quicknewsapp.secondary_fragments

import android.os.Bundle
import com.example.quicknewsapp.R
import com.example.quicknewsapp.models.Article

class OpenArticleConfirmFragment : ConfirmFragment() {

    override var confirmLabelStringId = R.string.confirmation_open_label

    override var confirmButtonStringId = R.string.confirmation_open_button_text

    companion object {
        fun newInstance(article: Article): ConfirmFragment {
            val fragment = OpenArticleConfirmFragment()
            val args = Bundle()
            args.putParcelable(ARTICLE_KEY, article)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onConfirmClicked() {
        mainActivity!!.onGoToArticleConfirmed(article)
    }
}