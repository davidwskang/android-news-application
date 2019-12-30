package com.davidwskang.quicknewsapp.secondary_fragments.settings

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.davidwskang.quicknewsapp.common.AppFragment
import com.davidwskang.quicknewsapp.R
import com.davidwskang.quicknewsapp.common.Constants
import com.davidwskang.quicknewsapp.models.LocationInformation
import com.davidwskang.quicknewsapp.secondary_fragments.settings.CountrySelectionView.HIGHLIGHT
import com.jwang123.flagkit.FlagKit
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings_country_selection_layout.*
import java.util.*

class SettingsFragment : AppFragment() {

    private var countryViewMap = HashMap<String, CountrySelectionView>()

    private lateinit var locationCopy : LocationInformation
    private lateinit var initLocation : LocationInformation

    companion object {
        const val COUNTRY_CODE_ROWS = 14
        const val COUNTRY_CODE_COLS = 4
        const val DROP_DOWN_ANIM_DUR = 200L
        const val DROP_DOWN_ROTATION_UP = 90f
        const val DROP_DOWN_ROTATION_DOWN = 270f

        const val LOCATION_KEY = "location"

        fun newInstance(location : LocationInformation): SettingsFragment {
            val args = Bundle()
            val fragment = SettingsFragment()
            args.putParcelable(LOCATION_KEY, location)
            fragment.arguments = args
            return fragment
        }
    }

    override val layoutId = R.layout.fragment_settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.run {
            initLocation = getParcelable(LOCATION_KEY)!!
            locationCopy = initLocation.copy()
        }

        init()
        initCountryList()
    }

    private fun init() {
        // Consume touch at this fragment
        bottom_drawer_menu_container.setOnTouchListener { v: View?, event: MotionEvent? -> true }

        settings_action_bar_icon.setOnClickListener { onBackButtonClicked() }
        setTransitions(country_selection_root)
        setTransitions(settings_content)

        selected_country_flag.setImageDrawable(FlagKit.drawableWithFlag(context, locationCopy.countryCode.toLowerCase(Locale.ROOT)))
        val countryIndex = Constants.COUNTRY_CODES.indexOf(locationCopy.countryCode.toLowerCase(Locale.ROOT))
        selected_country_name.text = Constants.COUNTRY_NAMES[countryIndex]

        country_selection_drop_down.setOnClickListener { v: View? ->
            val rotationVal: Float
            val durationVal: Long = DROP_DOWN_ANIM_DUR
            if (country_list.visibility == View.GONE) {
                country_selection_divider.visibility = View.VISIBLE
                country_list.visibility = View.VISIBLE
                rotationVal = DROP_DOWN_ROTATION_UP
            } else {
                country_selection_divider.visibility = View.GONE
                country_list.visibility = View.GONE
                rotationVal = DROP_DOWN_ROTATION_DOWN
            }
            country_drop_down_image.animate()
                    .rotation(rotationVal)
                    .setDuration(durationVal)
                    .start()
        }
    }

    private fun setTransitions(viewGroup: ViewGroup) {
        viewGroup.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        viewGroup.layoutTransition.setStartDelay(LayoutTransition.CHANGE_DISAPPEARING, 0)
    }

    private fun initCountryList() {
        for (rowIndex in 0 until COUNTRY_CODE_ROWS) {
            val row = LayoutInflater.from(context).inflate(R.layout.item_country_row, bottom_drawer_menu_container, false) as ViewGroup
            for (colIndex in 0 until COUNTRY_CODE_COLS) {
                val countryView = row.getChildAt(colIndex) as CountrySelectionView
                val index = rowIndex * COUNTRY_CODE_COLS + colIndex

                if (index >= Constants.COUNTRY_CODES.size) {
                    countryView.visibility = View.INVISIBLE
                    continue // let view be invisible for sizing
                }
                val countryCode = Constants.COUNTRY_CODES[rowIndex * COUNTRY_CODE_COLS + colIndex]

                countryView.apply {
                    visibility = View.VISIBLE
                    setBackgroundResource(R.drawable.background_chip_unselected)
                    val selectedCountryCode = locationCopy.countryCode
                    setCountryFlag(countryCode)
                    setCountryTitle(countryCode.toUpperCase(Locale.getDefault()))
                    setHighlight(
                            if (countryCode.equals(selectedCountryCode, ignoreCase = true)) HIGHLIGHT.SELECTED
                            else HIGHLIGHT.NOT_SELECTED)
                    setOnClickListener { v: View? -> onCountryCodeSelected(countryCode) }
                }
                countryViewMap[countryCode.toLowerCase(Locale.ROOT)] = countryView
            }
            country_list.addView(row)
        }
    }

    private fun onCountryCodeSelected(newCountryCode: String) {
        // remove highlight on current selected country chip
        countryViewMap[locationCopy.countryCode.toLowerCase()]?.setHighlight(HIGHLIGHT.NOT_SELECTED)

        // update class var
        locationCopy.countryCode = newCountryCode


        // add highlight on newly current selected country chip
        countryViewMap[locationCopy.countryCode.toLowerCase()]?.setHighlight(HIGHLIGHT.SELECTED)

        // update dropdown view
        selected_country_flag.setImageDrawable(FlagKit.drawableWithFlag(context, locationCopy.countryCode.toLowerCase(Locale.ROOT)))
        val countryIndex = Constants.COUNTRY_CODES.indexOf(locationCopy.countryCode.toLowerCase(Locale.ROOT))
        selected_country_name.text = Constants.COUNTRY_NAMES[countryIndex]


        // close the list
        country_list.visibility = View.GONE
    }

    private fun onBackButtonClicked() {
        mainActivity!!.supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_in_left_to_right, R.anim.anim_out_left_to_right)
                .remove(this)
                .commit()
    }
}