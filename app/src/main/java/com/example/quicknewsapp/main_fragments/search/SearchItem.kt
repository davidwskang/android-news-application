package com.example.quicknewsapp.main_fragments.search

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class SearchItem(

        @PrimaryKey
        public val searchTerm : String,

        @ColumnInfo(name = "date")
        public val date : Long // date in epoch time

) : Parcelable