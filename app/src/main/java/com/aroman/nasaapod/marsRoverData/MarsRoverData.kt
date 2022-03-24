package com.aroman.nasaapod.marsRoverData

sealed class MarsRoverData {
    data class Success(val serverResponseData: MarsRoverServerResponseData) : MarsRoverData()
    data class Error(val error: Throwable) : MarsRoverData()
    data class Loading(val progress: Int?) : MarsRoverData()
}