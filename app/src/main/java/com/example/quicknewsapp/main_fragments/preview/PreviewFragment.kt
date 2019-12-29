package com.example.quicknewsapp.main_fragments.preview

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.room.Room
import com.example.quicknewsapp.common.AppFragment
import com.example.quicknewsapp.R
import com.example.quicknewsapp.main_fragments.bookmarks.BookmarkedArticlesDatabase
import com.example.quicknewsapp.models.Article
import com.example.quicknewsapp.common.Constants
import com.example.quicknewsapp.util.AppUtils
import com.squareup.picasso.Picasso
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_preview.*
import org.joda.time.DateTime
import timber.log.Timber

class PreviewFragment : AppFragment() {

    override val layoutId = R.layout.fragment_preview
    private var article: Article? = null

    companion object {

        const val ARTICLE_KEY = "article"
        const val IMAGE_HEIGHT_DP = 240f // set to 240dp

        const val SELECTED_BOOKMARK_IMG = R.drawable.ic_bookmark_black_35dp
        const val UNSELECTED_BOOKMARK_IMG = R.drawable.ic_bookmark_border_black_35dp

        @JvmStatic
        fun newInstance(article: Article?): PreviewFragment {
            val args = Bundle()
            args.putParcelable(ARTICLE_KEY, article)
            val fragment = PreviewFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottom_drawer_menu_container.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        arguments?.run {
            article = getParcelable(ARTICLE_KEY)
        }

        article?.run {

            val imageHeight = AppUtils.convertDipToPx(context!!, IMAGE_HEIGHT_DP)
            val imageWidth = mainActivity?.screenWidth!!

            Picasso.get()
                    .load(imageUrl)
                    .resize(imageWidth, imageHeight)
                    .centerCrop()
                    .into(preview_image)

            preview_title.text = title
            preview_content.text = content
            preview_date.text = publishedDate
            save_article_button.setImageResource(
                    if (isBookmarked) SELECTED_BOOKMARK_IMG
                    else UNSELECTED_BOOKMARK_IMG
            )
        }

        save_article_button.setOnClickListener {
            article?.run {
                if (isBookmarked) {
                    save_article_button.setImageResource(UNSELECTED_BOOKMARK_IMG)
                } else {
                    save_article_button.setImageResource(SELECTED_BOOKMARK_IMG)
                }
                isBookmarked = !isBookmarked
            }
        }

        bottom_drawer_menu_container.setOnTouchListener(DismissalTouchListener().getTouchListener())
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        var anim = super.onCreateAnimation(transit, enter, nextAnim)

        if (anim == null && nextAnim != 0) {
            anim = AnimationUtils.loadAnimation(activity, nextAnim);
        }
        if (anim != null) {
            view?.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        }

        anim?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                view?.setLayerType(View.LAYER_TYPE_NONE, null)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {}
        })
        return anim
    }

    private fun dismissPreviewFragment() {
        mainActivity!!.supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.anim_in_bot_to_top, R.anim.anim_out_top_to_bot)
                .remove(this)
                .commit()
    }

    private fun getSavedArticlesDatabase(): BookmarkedArticlesDatabase {
        return Room.databaseBuilder(activity!!.applicationContext,
                BookmarkedArticlesDatabase::class.java, Constants.SAVED_DB).build()
    }

    inner class DismissalTouchListener() {

        private val DISMISSAL_THRESHOLD = 0.45f
        private val ANIMATION_DURATION = 100L
        private val BASE_Y = 0f
        private val BASE_SCALE = 1f

        var rootStart = 0f
        var touchStart = 0f

        fun getTouchListener(): View.OnTouchListener {
            return View.OnTouchListener { v, event ->
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> onDownEvent(event)
                    MotionEvent.ACTION_MOVE -> onMoveEvent(event)
                    MotionEvent.ACTION_UP -> onUpEvent(event)
                }
                true
            }
        }

        private fun onDownEvent(event: MotionEvent) {
            rootStart = bottom_drawer_menu_container.y
            touchStart = event.rawY
        }

        private fun onMoveEvent(event: MotionEvent) {
            val diffY = event.rawY - touchStart
            val percentMoved = diffY / mainActivity!!.screenHeight.toFloat()
            val newY = rootStart + diffY

            if (newY > rootStart) {
                bottom_drawer_menu_container.animate()
                        .y(newY)
                        .scaleX(BASE_SCALE - (percentMoved / 3)) // scaled by a third
                        .scaleY(BASE_SCALE - (percentMoved / 3)) // scaled by a third
                        .setDuration(0)
                        .start()
            }
        }

        private fun onUpEvent(event: MotionEvent) {
            val diffY = event.rawY - touchStart
            val percentMoved = diffY / mainActivity!!.screenHeight.toFloat()

            if (percentMoved > DISMISSAL_THRESHOLD) {
                dismissPreviewFragment()

            } else {
                bottom_drawer_menu_container.animate()
                        .setDuration(ANIMATION_DURATION)
                        .translationY(BASE_Y)
                        .scaleX(BASE_SCALE)
                        .scaleY(BASE_SCALE)
                        .start()
            }
        }
    }
}