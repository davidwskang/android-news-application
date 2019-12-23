package com.example.quicknewsapp.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.quicknewsapp.R
import kotlinx.android.synthetic.main.layout_bottom_drawer.view.*
import kotlin.math.exp
import kotlin.math.sqrt


class BottomDrawerLayout : ConstraintLayout {

    var isOpen : Boolean = false
    private lateinit var fadeInAnim : Animation
    private lateinit var fadeOutAnim : Animation

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

    fun setBottomDrawerMenuSelectedListener(listener : BottomDrawerMenuContainer.BottomDrawerMenuItemSelectedListener) {
        bottom_drawer_menu_container.listener = listener
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return  if (isOpen) super.dispatchTouchEvent(ev)
                else false
    }

}

class BottomDrawerMenuContainer : LinearLayout {

    private lateinit var openAnim : Animation
    private lateinit var closeAnim : Animation
    var listener : BottomDrawerMenuItemSelectedListener? = null
    lateinit var drawerLayout : BottomDrawerLayout
    private var targetY = -1f
    private var rootStart = 0f
    private var initX = 0f
    private var initY = 0f
    private val boundsArray = ArrayList<Rect>()

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        openAnim = AnimationUtils.loadAnimation(context, R.anim.anim_in_translate_bottom_drawer)
        closeAnim = AnimationUtils.loadAnimation(context, R.anim.anim_out_translate_bottom_drawer)
    }

    override fun onAnimationStart() {
        super.onAnimationStart()
        // set target Y value for animations. (In the case that this Y value is changed by other anim)
        if (targetY == -1f) {
            targetY = y
        }
    }

    override fun onAnimationEnd() {
        super.onAnimationEnd()
        y = targetY
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        val ret = super.drawChild(canvas, child, drawingTime)
        if (boundsArray.size < childCount) {
            child?.run {
                val rect = Rect(left, top, right, bottom)
                boundsArray.add(rect)
            }
        }
        return ret
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                initX = event.rawX
                initY = event.rawY
                rootStart = bottom_drawer_menu_container.y

            }
            MotionEvent.ACTION_MOVE -> {
                val diffY = event.rawY - initY
                val newY = rootStart + diffY
                if (newY > rootStart) {
                    bottom_drawer_menu_container.animate()
                            .y(newY)
                            .setDuration(0)
                            .start()
                }
            }
            MotionEvent.ACTION_UP -> {
                val exitX = event.rawX
                val exitY = event.rawY
                val dist = sqrt(exp(exitX - initX) + exp(exitY - initY))

                if (dist < 10) {
                    val position = withinBounds(event.x, event.y)
                    if (position > -1) {

                        listener!!.onBottomDrawerMenuItemSelected(position)

                    }
                } else {
                    val diffY = event.rawY - initY
                    val percentDragged = diffY.div(height)

                    if (percentDragged > 0.1f) {
                        drawerLayout.closeDrawer()

                    } else {
                        val duration = 1f.minus(percentDragged).times(100)
                        bottom_drawer_menu_container.animate()
                                .setDuration(duration.toLong())
                                .translationY(0f)
                                .start()
                    }
                }
                initX = 0f
                initY = 0f
            }
        }
        return true
    }

    fun show() = startAnimation(openAnim)

    fun dismiss() = startAnimation(closeAnim)

    private fun withinBounds(x : Float, y : Float) : Int {
        for (i in 0 until boundsArray.size) {
            if (boundsArray[i].contains(x.toInt(), y.toInt())){
                return i
            }
        }
        return -1
    }

    interface BottomDrawerMenuItemSelectedListener {
        fun onBottomDrawerMenuItemSelected(position : Int)
    }

}
