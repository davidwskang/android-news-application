package com.davidwskang.quicknewsapp.models

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

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Article

                if (source != other.source) return false
                if (author != other.author) return false
                if (title != other.title) return false
                if (description != other.description) return false
                if (articleUrl != other.articleUrl) return false
                if (publishedDate != other.publishedDate) return false
                if (imageUrl != other.imageUrl) return false
                if (content != other.content) return false
                return true
        }

        override fun hashCode(): Int {
                var result = source?.hashCode() ?: 0
                result = 31 * result + (author?.hashCode() ?: 0)
                result = 31 * result + title.hashCode()
                result = 31 * result + (description?.hashCode() ?: 0)
                result = 31 * result + (articleUrl?.hashCode() ?: 0)
                result = 31 * result + (publishedDate?.hashCode() ?: 0)
                result = 31 * result + (imageUrl?.hashCode() ?: 0)
                result = 31 * result + (content?.hashCode() ?: 0)
                return result
        }


}