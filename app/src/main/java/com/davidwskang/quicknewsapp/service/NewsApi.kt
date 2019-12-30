package com.davidwskang.quicknewsapp.service

import com.davidwskang.quicknewsapp.model.Feed
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsApi {

    @Headers("Content-Type: application/json")
    @GET("top-headlines")
    fun getTopHeadlines(@Query("country") country : String,
                        @Query("category") category : String,
                        @Query("page") page : Int,
                        @Query("apiKey") apiKey : String): Single<Feed>

    @Headers("Content-Type: application/json")
    @GET("everything")
    fun getEverything(
            @Query("q") searchTerm : String,
            @Query("page") page : Int,
            @Query("apiKey") apiKey : String
    ) : Single<Feed>

}