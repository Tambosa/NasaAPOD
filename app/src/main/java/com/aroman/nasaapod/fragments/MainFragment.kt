package com.aroman.nasaapod.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.transition.*
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import coil.load
import com.aroman.nasaapod.*
import com.aroman.nasaapod.activities.ApiRoverActivity
import com.aroman.nasaapod.activities.MainActivity
import com.aroman.nasaapod.activities.RecyclerNotesActivity
import com.aroman.nasaapod.podData.POD_Data
import com.aroman.nasaapod.podData.POD_ViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_main.bottom_app_bar
import kotlinx.android.synthetic.main.fragment_main.chip_before_yesterday
import kotlinx.android.synthetic.main.fragment_main.chip_group
import kotlinx.android.synthetic.main.fragment_main.chip_today
import kotlinx.android.synthetic.main.fragment_main.chip_yesterday
import kotlinx.android.synthetic.main.fragment_main.fab
import kotlinx.android.synthetic.main.fragment_main.image_view
import kotlinx.android.synthetic.main.fragment_main.input_edit_text
import kotlinx.android.synthetic.main.fragment_main.input_layout
import kotlinx.android.synthetic.main.fragment_main.loadingLayout
import kotlinx.android.synthetic.main.fragment_main.motion_layout_main
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {

    private val viewModel: POD_ViewModel by viewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var isExpanded = false

    companion object {
        fun newInstance() = MainFragment()
        private var isMain = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main_start, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val date = dateFormat.format(Date())

        viewModel.getData(date).observe(viewLifecycleOwner) { renderData(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        input_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${input_edit_text.text.toString()}")
            })
        }
        setBottomSheetBehavior()
        setBottomAppBar(view)

        chip_group.setOnCheckedChangeListener { _, checkedID: Int ->
            isExpanded = false
            when (checkedID) {
                chip_today.id -> viewModel.getData(dateFormat.format(Date()))
                chip_yesterday.id -> viewModel.getData(getYesterdayDateString())
                chip_before_yesterday.id -> viewModel.getData(getBeforeYesterdayDateString())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_telescope -> activity?.let {
                startActivity(Intent(it, ApiRoverActivity::class.java))
            }

            R.id.app_bar_fav -> activity?.let {
                startActivity(Intent(it, RecyclerNotesActivity::class.java))
            }

            R.id.app_bar_settings -> activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.main_container, SettingsFragment())?.addToBackStack("")?.commit()

            android.R.id.home -> {
                Log.d("MENU", "hamburger is clicked ")
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
        initFab()
    }

    private fun initFab() {
        fab.setOnClickListener {
            if (isMain) {
                isMain = false
                bottom_app_bar.navigationIcon = null
                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_back_fab
                    )
                )
                bottom_app_bar.replaceMenu(R.menu.secondary_menu)
            } else {
                isMain = true
                bottom_app_bar.navigationIcon =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_hamburger_menu_bottom_bar
                    )
                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_plus_fab
                    )
                )
                bottom_app_bar.replaceMenu(R.menu.main_menu)
            }
        }
    }

    private fun getBeforeYesterdayDateString(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -2)
        return dateFormat.format(calendar.time)
    }

    private fun getYesterdayDateString(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return dateFormat.format(calendar.time)
    }

    private fun setBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_container)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun renderData(data: POD_Data) {
        when (data) {
            is POD_Data.Success -> {
                loadingLayout.visibility = View.GONE

                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    throw Exception("url is null or empty")
                } else {
                    image_view.load(url) {
                        lifecycle(this@MainFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    image_view.visibility = View.VISIBLE

                    val spannable = SpannableString(serverResponseData.title)
                    spannable.apply {
                        setSpan(
                            ForegroundColorSpan(Color.parseColor("#F97C3C")),
                            0,
                            (spannable.length / 5),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        setSpan(
                            ForegroundColorSpan(Color.parseColor("#FDB54E")),
                            0 + (spannable.length / 5),
                            (spannable.length / 5) * 2,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        setSpan(
                            ForegroundColorSpan(Color.parseColor("#64B678")),
                            0 + (spannable.length / 5) * 2,
                            (spannable.length / 5) * 3,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        setSpan(
                            ForegroundColorSpan(Color.parseColor("#478AEA")),
                            0 + (spannable.length / 5) * 3,
                            (spannable.length / 5) * 4,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        setSpan(
                            ForegroundColorSpan(Color.parseColor("#8446CC")),
                            0 + (spannable.length / 5) * 4,
                            spannable.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    bottom_sheet_description_header.text = spannable
                    bottom_sheet_description.text = serverResponseData.explanation

                    image_view.setOnClickListener { initExpansionAnimation() }
                }
            }

            is POD_Data.Loading -> {
                image_view.visibility = View.INVISIBLE
                bottom_sheet_description_header.text = ""
                bottom_sheet_description.text = ""
                loadingLayout.visibility = View.VISIBLE
            }

            is POD_Data.Error -> {
                loadingLayout.visibility = View.GONE
            }
        }
    }

    private fun initExpansionAnimation() {
        isExpanded = !isExpanded
        TransitionManager.beginDelayedTransition(
            motion_layout_main, TransitionSet()
                .addTransition(ChangeBounds())
                .addTransition(ChangeImageTransform())
        )
        val params: ViewGroup.LayoutParams = image_view.layoutParams
        params.height = if (isExpanded) ViewGroup.LayoutParams.MATCH_PARENT
        else ViewGroup.LayoutParams.WRAP_CONTENT
        image_view.layoutParams = params
        image_view.scaleType = if (isExpanded) ImageView.ScaleType.CENTER_CROP
        else ImageView.ScaleType.FIT_CENTER
    }
}
