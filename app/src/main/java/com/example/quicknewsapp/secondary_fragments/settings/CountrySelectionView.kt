package com.example.quicknewsapp.secondary_fragments.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.quicknewsapp.R
import com.jwang123.flagkit.FlagKit
import kotlinx.android.synthetic.main.item_country_single.view.*
import java.util.*

class CountrySelectionView : ConstraintLayout {

    enum class HIGHLIGHT {
        SELECTED, NOT_SELECTED, NONE
    }

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context?) {
        View.inflate(context, R.layout.item_country_single, this)
    }

    fun setCountryFlag(countryCode: String) {
        val flagDrawable = FlagKit.drawableWithFlag(context, countryCode.toLowerCase(Locale.ROOT))
        flag_image.setImageDrawable(flagDrawable)
    }

    fun setCountryTitle(title : String) {
        country_title.text = title
    }

    fun setHighlight(highlightType: HIGHLIGHT) {
        when (highlightType) {
            HIGHLIGHT.NOT_SELECTED -> setBackgroundResource(R.drawable.background_chip_unselected)
            HIGHLIGHT.SELECTED -> setBackgroundResource(R.drawable.background_chip_selected)
            else -> setBackgroundResource(0)
        }
    }
}