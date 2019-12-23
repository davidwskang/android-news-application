package com.example.quicknewsapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Feed(
    var status : String,
    var totalResults : Int,
    var articles : List<Article>
) : Parcelable