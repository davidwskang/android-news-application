package com.davidwskang.quicknewsapp.models

import android.os.Parcelable
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Source(
        val id : String?,
        val name : String?
) : Parcelable

class Converter {

    @TypeConverter
    fun fromSourceToString(value : Source?) : String? {
        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        val adapter = moshi.adapter(Source::class.java)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun fromStringToSource(value : String?) : Source? {
        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        val adapter = moshi.adapter(Source::class.java)
        return adapter.nullSafe().fromJson(value!!)
    }
}
