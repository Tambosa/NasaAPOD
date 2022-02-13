package com.aroman.nasaapod.api

import com.google.gson.annotations.SerializedName

data class MarsRoverServerResponseData(
    @field:SerializedName("photos") val photos: ArrayList<MarsRoverServerResponsePhoto>
)

data class MarsRoverServerResponsePhoto(
    @field:SerializedName("id") val id: String?,
    @field:SerializedName("sol") val sol: String?,
    @field:SerializedName("img_src") val img_src: String?,
    @field:SerializedName("earth_date") val earth_date: String?
)
