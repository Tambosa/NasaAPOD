package com.aroman.nasaapod.marsRoverData

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MarsRoverApi {

    /** https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=2015-6-3&api_key=DEMO_KEY */

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getCuriosityPhoto(
        @Query("earth_date") earthDate: String,
        @Query("camera") camera: String = "NAVCAM",
        @Query("api_key") apiKey: String
    ): Call<MarsRoverServerResponseData>

    @GET("mars-photos/api/v1/rovers/opportunity/photos")
    fun getOpportunityPhoto(
        @Query("earth_date") earthDate: String,
        @Query("camera") camera: String = "NAVCAM",
        @Query("api_key") apiKey: String
    ): Call<MarsRoverServerResponseData>

    @GET("mars-photos/api/v1/rovers/spirit/photos")
    fun getSpiritPhoto(
        @Query("earth_date") earthDate: String,
        @Query("camera") camera: String = "NAVCAM",
        @Query("api_key") apiKey: String
    ): Call<MarsRoverServerResponseData>
}