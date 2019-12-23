package com.example.quicknewsapp.util

import android.content.Context
import android.os.IBinder
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlinx.android.synthetic.main.view_action_bar.view.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.concurrent.TimeUnit

class AppUtils {

    companion object {

        /**
         *  Return the date for viewing in the previews.
         *  if date is between 0-59 (inclusive) minutes ago, then this function returns "x minutes ago"
         *  if date is between 1-23 (inclusive) hours ago, then this function returns "x hours ago".
         *  if date is between 1-6 (inclusive) days ago, then this function returns "x days ago"
         *  otherwise, returns "dd MMM yyyy"
         */
        fun getDate(date : String) : String {
            val formatterInput = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ")
            val inputDate = DateTime.parse(date, formatterInput)

            val now = DateTime.now()
            val difference =  now.millis - inputDate.millis

            val minutesAgo = TimeUnit.MILLISECONDS.toMinutes(difference)
            val hoursAgo = minutesAgo / 60
            val daysAgo = TimeUnit.MILLISECONDS.toDays(difference)

            if (minutesAgo < 60) {
                return "$minutesAgo min(s) ago"
            } else if (hoursAgo < 24) {
                return "$hoursAgo hour(s) ago"
            } else if (daysAgo < 7) {
                return "$daysAgo day(s) ago"
            } else {
                val formatterOutput = DateTimeFormat.forPattern("dd MMM yyyy")
                val outputDate = inputDate.toString(formatterOutput)
                return outputDate.toString()
            }
        }

        fun hideSoftKeyboard(context : Context?, windowToken : IBinder?) {
            val inputManager =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(windowToken, 0)
        }

        fun convertDipToPx(context : Context, dips : Float) : Int {
            val metrics = context.resources.displayMetrics
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips, metrics).toInt()
        }

    }

}