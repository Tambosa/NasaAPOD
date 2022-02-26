package com.aroman.nasaapod.podData

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aroman.nasaapod.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class POD_ViewModel(
    private val liveDataForViewToObserve: MutableLiveData<POD_Data> = MutableLiveData(),
    private val retrofitImpl: POD_RetrofitImpl = POD_RetrofitImpl()
) : ViewModel() {

    fun getData(date: String): LiveData<POD_Data> {
        sendServerRequest(date)
        return liveDataForViewToObserve
    }

    private fun sendServerRequest(date: String) {
        liveDataForViewToObserve.value = POD_Data.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY

        if (apiKey.isBlank()) {
            POD_Data.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPodDate(date, apiKey).enqueue(
                object : Callback<POD_ServerResponseData> {
                    override fun onResponse(
                        call: Call<POD_ServerResponseData>,
                        response: Response<POD_ServerResponseData>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataForViewToObserve.value =
                                POD_Data.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveDataForViewToObserve.value =
                                    POD_Data.Error(Throwable("message isNullOrEmpty"))
                            } else {
                                liveDataForViewToObserve.value =
                                    POD_Data.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<POD_ServerResponseData>, t: Throwable) {
                        liveDataForViewToObserve.value = POD_Data.Error(t)
                    }
                })
        }
    }
}
