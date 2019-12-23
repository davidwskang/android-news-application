package com.example.quicknewsapp.api

import com.example.quicknewsapp.common.Constants
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitClient {

    companion object {

        private var newsClient : Retrofit? = null
        private var locationClient : Retrofit? = null

        fun getNewsInstance() : Retrofit {
            if (newsClient == null) {
                newsClient = Retrofit.Builder()
                        .baseUrl(Constants.NEWS_API_BASE_URL)
                        .addConverterFactory(MoshiConverterFactory.create(getMoshi()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
            }
            return newsClient!!
        }

        fun getLocationInstance() : Retrofit {
            if (locationClient == null) {
                locationClient = Retrofit.Builder()
                        .baseUrl(Constants.LOCATION_BASE_URL)
                        .addConverterFactory(MoshiConverterFactory.create(getMoshi()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
            }
            return locationClient!!
        }

        private fun getMoshi() : Moshi {
            return Moshi.Builder()
                    .add(NullToEmptyStringAdapter)
                    .add(KotlinJsonAdapterFactory())
                    .build()
        }

    }

    object NullToEmptyStringAdapter {
        @FromJson
        fun fromJson(reader: JsonReader): String {
            if (reader.peek() != JsonReader.Token.NULL) {
                return reader.nextString()
            }
            reader.nextNull<Unit>()
            return Constants.EMPTY_STRING
        }
    }
}