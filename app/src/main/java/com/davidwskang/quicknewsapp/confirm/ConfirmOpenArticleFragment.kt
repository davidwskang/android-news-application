package com.davidwskang.quicknewsapp.confirm

import android.os.Bundle
import com.davidwskang.quicknewsapp.R
import com.davidwskang.quicknewsapp.model.Article

class ConfirmOpenArticleFragment : ConfirmFragment() {

    override var confirmLabelStringId = R.string.confirmation_open_label

    override var confirmButtonStringId = R.string.confirmation_open_button_text

    companion object {
        fun newInstance(article: Article): ConfirmFragment {
            val fragment = ConfirmOpenArticleFragment()
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