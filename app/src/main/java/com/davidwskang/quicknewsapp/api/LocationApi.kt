package com.davidwskang.quicknewsapp.api

import com.davidwskang.quicknewsapp.models.LocationInformation
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

interface LocationApi {

    @Headers("Content-Type: application/json")
    @GET("json")
    fun getLocation() : Single<LocationInformation>

}