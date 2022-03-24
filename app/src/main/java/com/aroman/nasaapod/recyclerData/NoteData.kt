package com.aroman.nasaapod.recyclerData

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class NoteData(
    var content: String = "New task 1",
    var date: Date = Calendar.getInstance().time
) : Parcelable