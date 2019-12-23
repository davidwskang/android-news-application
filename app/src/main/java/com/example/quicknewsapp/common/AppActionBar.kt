package com.example.quicknewsapp.common

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.quicknewsapp.main_fragments.AppMainFragment
import com.example.quicknewsapp.R
import com.example.quicknewsapp.util.AppUtils
import kotlinx.android.synthetic.main.view_action_bar.view.*


class AppActionBar : ConstraintLayout, TextWatcher, OnEditorActionListener {

    var listener: ActionBarClickListener? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.view_action_bar, this)
        left_icon.setOnClickListener { listener?.onLeftIconActionBarClicked() }
        right_icon.setOnClickListener { listener?.onRightIconActionBarClicked() }
        search_input.addTextChangedListener(this)
        search_input.setOnEditorActionListener(this)
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        header_text.visibility = if (s.toString().isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId != EditorInfo.IME_ACTION_SEARCH || listener == null) {
            return false
        }

        AppUtils.hideSoftKeyboard(context, windowToken)

        search_input.run {
            clearFocus()
            listener!!.onSearchByActionBar(text.toString())
            setText(Constants.EMPTY_STRING)
        }

        return true
    }

    fun updateActionBar(newFrag: AppMainFragment) {
        newFrag.run {
            left_icon.setImageResource(actionBarIconId)
            search_input.visibility = actionBarSearchVisibility
            header_text.text = actionBarTitle
        }
    }

    fun setActionBarClickListener(listener: ActionBarClickListener?) {
        this.listener = listener
    }

    interface ActionBarClickListener {

        fun onLeftIconActionBarClicked()

        fun onRightIconActionBarClicked()

        fun onSearchByActionBar(searchTerm: String)
    }
}