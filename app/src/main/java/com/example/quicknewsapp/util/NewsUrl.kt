package com.example.quicknewsapp.util

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.lang.StringBuilder

@Parcelize
data class NewsUrl(
        var type : FeedType,
        var searchTerm : String = "",
        var countryCode : String = "",
        var category : String ="",
        var page : Int = 1

) : Parcelable {

    enum class FeedType {
        SEARCH, HEADLINES
    }

    fun build() : String {
        val builder = StringBuilder("https://newsapi.org/v2/")

        if (type == FeedType.SEARCH) {
            builder.append("everything?")
            builder.append("q=$searchTerm&")
        } else {
            builder.append("top-headlines?")
            builder.append("country=$countryCode&")
            builder.append("category=$category&")
        }
        builder.append("page=$page&")
        builder.append("apiKey=e1217cf1e1fc42d3bfe96c84039f7170")
        return builder.toString()
    }
}