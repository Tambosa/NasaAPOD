package com.aroman.nasaapod.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import coil.load
import com.aroman.nasaapod.marsRoverData.MarsRoverData
import com.aroman.nasaapod.marsRoverData.MarsRoverViewModel
import com.aroman.nasaapod.R
import kotlinx.android.synthetic.main.fragment_curiosity.*
import kotlinx.android.synthetic.main.fragment_opportunity.*
import java.text.SimpleDateFormat
import java.util.*

class OpportunityFragment : Fragment() {
    private val viewModel: MarsRoverViewModel by viewModels()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_opportunity, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //val date = dateFormat.format(Date())
        //date is hardcoded. Photos are not posted every day. TODO fix later
        viewModel.getOpportunityData("2018-03-6").observe(this@OpportunityFragment, { renderData(it) })
    }

    private fun renderData(data: MarsRoverData) {
        when (data) {
            is MarsRoverData.Success -> {
                val serverResponseData = data.serverResponseData
                var imgSrc = serverResponseData.photos[0].img_src
                if (imgSrc.isNullOrEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "No photos today from Curiosity",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    opportunity_imageview.load(imgSrc) {
                        lifecycle(this@OpportunityFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }

                    var iterator = 0
                    opportunity_imageview.setOnClickListener {
                        imgSrc = serverResponseData.photos[iterator++].img_src
                        opportunity_imageview.load(imgSrc) {
                            lifecycle(this@OpportunityFragment)
                            error(R.drawable.ic_load_error_vector)
                            placeholder(R.drawable.ic_no_photo_vector)
                        }
                        if (iterator == serverResponseData.photos.size) iterator = 0
                    }
                }
            }
            is MarsRoverData.Loading -> {
                //nothing
            }
            is MarsRoverData.Error -> {
                //nothing
            }
        }
    }
}