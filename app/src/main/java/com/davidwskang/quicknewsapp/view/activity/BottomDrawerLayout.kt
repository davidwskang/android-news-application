package com.davidwskang.quicknewsapp.view.activity

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import com.davidwskang.quicknewsapp.R
import kotlinx.android.synthetic.main.layout_bottom_drawer.view.*


class BottomDrawerLayout : ConstraintLayout {

    var isOpen: Boolean = false
    private lateinit var fadeInAnim: Animation
    private lateinit var fadeOutAnim: Animation

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.layout_bottom_drawer, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        fadeInAnim = AnimationUtils.loadAnimation(context, R.anim.anim_in_fade_bottom_drawer_)
        fadeOutAnim = AnimationUtils.loadAnimation(context, R.anim.anim_out_fade_bottom_drawer)

        visibility = GONE

        bottom_drawer_layout_background.setOnClickListener {
            closeDrawer()
        }
        bottom_drawer_menu_container.drawerLayout = this
    }

    fun openDrawer() {
        if (isOpen) return
        visibility = View.VISIBLE
        bottom_drawer_menu_container.show()
        bottom_drawer_layout_background.startAnimation(fadeInAnim)
        isOpen = true
    }

    fun closeDrawer() {
        if (!isOpen) return
        bottom_drawer_menu_container.dismiss()
        bottom_drawer_layout_background.startAnimation(fadeOutAnim)
        isOpen = false
    }

    fun setBottomDrawerMenuSelectedListener(listener: BottomDrawerMenuContainer.BottomDrawerMenuItemSelectedListener) {
        bottom_drawer_menu_container.listener = listener
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return if (isOpen) super.dispatchTouchEvent(ev)
        else false
    }

}


