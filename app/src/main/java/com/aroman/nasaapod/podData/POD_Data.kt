package com.aroman.nasaapod.podData

sealed class POD_Data {
    data class Success(val serverResponseData: POD_ServerResponseData) : POD_Data()
    data class Error(val error: Throwable) : POD_Data()
    data class Loading(val progress: Int?) : POD_Data()
}
