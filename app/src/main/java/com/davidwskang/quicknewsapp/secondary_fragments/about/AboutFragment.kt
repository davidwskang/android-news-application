package com.davidwskang.quicknewsapp.secondary_fragments.about

import android.os.Bundle
import android.view.View
import com.davidwskang.quicknewsapp.common.AppFragment
import com.davidwskang.quicknewsapp.R
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : AppFragment() {

    override val layoutId = R.layout.fragment_about

    companion object {
        fun newInstance() : AboutFragment {
            val args = Bundle()
            val fragment = AboutFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottom_drawer_menu_container.setOnTouchListener { _, _ -> true }
        about_action_bar_icon.setOnClickListener { removeAboutFragment() }
    }

    private fun removeAboutFragment() {
        mainActivity!!.supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.anim_in_left_to_right, R.anim.anim_out_left_to_right)
                .remove(this)
                .commit()
    }

}