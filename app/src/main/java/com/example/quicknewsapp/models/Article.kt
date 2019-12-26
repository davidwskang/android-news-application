package com.example.quicknewsapp.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import org.joda.time.DateTime

@Parcelize
@Entity
@JsonClass(generateAdapter = true)
data class Article(

        @ColumnInfo(name = "source")
        @Json(name = "source")
        var source : Source?,

        @ColumnInfo(name = "author")
        @Json(name = "author")
        var author : String?,

        @PrimaryKey
        @Json(name = "title")
        var title : String,

        @ColumnInfo(name = "description")
        @Json(name = "description")
        var description : String?,

        @ColumnInfo(name = "articleUrl")
        @Json(name = "url")
        var articleUrl : String?,

        @ColumnInfo(name = "publishedAt")
        @Json(name = "publishedAt")
        var publishedDate : String?,

        @ColumnInfo(name = "imageUrl")
        @Json(name = "urlToImage")
        var imageUrl : String?,

        @ColumnInfo(name = "content")
        @Json(name = "content")
        var content : String?,

        @ColumnInfo(name = "bookmarkedDate")
        @Json(name = "bookmarkedDate") // does not come from NewsAPI
        var bookmarkedDate : Long = 0L,

        @ColumnInfo(name = "isBookmarked")
        @Json(name = "isBookmarked") // does not come from NewsAPI
        var isBookmarked : Boolean = false

) : Parcelable {

        fun prepareForInsert() {
                bookmarkedDate = DateTime.now().millis
                isBookmarked = true
        }

        fun prepareForDelete() {
                bookmarkedDate = 0L
                isBookmarked = false
        }
}