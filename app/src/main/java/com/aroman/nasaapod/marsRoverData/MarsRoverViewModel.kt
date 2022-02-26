package com.aroman.nasaapod.marsRoverData

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aroman.nasaapod.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsRoverViewModel(
    private val liveDataForViewToObserve: MutableLiveData<MarsRoverData> = MutableLiveData(),
    private val retrofitImpl: MarsRoverRetrofitImpl = MarsRoverRetrofitImpl()
) : ViewModel() {

    fun getCuriosityData(date: String): LiveData<MarsRoverData> {
        liveDataForViewToObserve.value = MarsRoverData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY

        if (apiKey.isBlank()) {
            MarsRoverData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getCuriosityPhoto(date, "NAVCAM", apiKey).enqueue(
                object : Callback<MarsRoverServerResponseData> {
                    override fun onResponse(
                        call: Call<MarsRoverServerResponseData>,
                        response: Response<MarsRoverServerResponseData>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataForViewToObserve.value =
                                MarsRoverData.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveDataForViewToObserve.value =
                                    MarsRoverData.Error(Throwable("message isNullOrEmpty"))
                            } else {
                                liveDataForViewToObserve.value =
                                    MarsRoverData.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<MarsRoverServerResponseData>, t: Throwable) {
                        liveDataForViewToObserve.value = MarsRoverData.Error(t)
                    }
                })
        }
        return liveDataForViewToObserve
    }

    fun getOpportunityData(date: String): LiveData<MarsRoverData> {
        liveDataForViewToObserve.value = MarsRoverData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY

        if (apiKey.isBlank()) {
            MarsRoverData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getOpportunityPhoto(date, "NAVCAM", apiKey).enqueue(
                object : Callback<MarsRoverServerResponseData> {
                    override fun onResponse(
                        call: Call<MarsRoverServerResponseData>,
                        response: Response<MarsRoverServerResponseData>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataForViewToObserve.value =
                                MarsRoverData.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveDataForViewToObserve.value =
                                    MarsRoverData.Error(Throwable("message isNullOrEmpty"))
                            } else {
                                liveDataForViewToObserve.value =
                                    MarsRoverData.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<MarsRoverServerResponseData>, t: Throwable) {
                        liveDataForViewToObserve.value = MarsRoverData.Error(t)
                    }
                })
        }
        return liveDataForViewToObserve
    }

    fun getSpiritData(date: String): LiveData<MarsRoverData> {
        liveDataForViewToObserve.value = MarsRoverData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY

        if (apiKey.isBlank()) {
            MarsRoverData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getSpiritPhoto(date, "NAVCAM", apiKey).enqueue(
                object : Callback<MarsRoverServerResponseData> {
                    override fun onResponse(
                        call: Call<MarsRoverServerResponseData>,
                        response: Response<MarsRoverServerResponseData>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataForViewToObserve.value =
                                MarsRoverData.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveDataForViewToObserve.value =
                                    MarsRoverData.Error(Throwable("message isNullOrEmpty"))
                            } else {
                                liveDataForViewToObserve.value =
                                    MarsRoverData.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<MarsRoverServerResponseData>, t: Throwable) {
                        liveDataForViewToObserve.value = MarsRoverData.Error(t)
                    }
                })
        }
        return liveDataForViewToObserve
    }
}