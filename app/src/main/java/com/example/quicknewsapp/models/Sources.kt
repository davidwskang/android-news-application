package com.example.quicknewsapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sources(
        val status : String,
        val sources: List<Source>
) : Parcelable