package com.aroman.nasaapod.podData

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface POD_API {
    /** Date must be YYYY_MM_DD for example 2022-02-03 */
    @GET("planetary/apod")
    fun getPodDate(
        @Query("date") date: String,
        @Query("api_key") apiKey: String
    ): Call<POD_ServerResponseData>
}