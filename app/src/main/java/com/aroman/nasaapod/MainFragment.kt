package com.aroman.nasaapod

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.load
import com.aroman.nasaapod.databinding.FragmentMainBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PictureOfTheDayViewModel by viewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    companion object {
        fun newInstance() = MainFragment()
        private var isMain = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val date = dateFormat.format(Date())

        viewModel.getData(date)
            .observe(this@MainFragment, Observer<PictureOfTheDayData> { renderData(it) })
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
            R.id.app_bar_fav -> Toast.makeText(context, "Favourite", Toast.LENGTH_SHORT).show()

            R.id.app_bar_settings -> activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.main_container, SettingsFragment())?.addToBackStack("")?.commit()

            R.id.home -> {
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
                fab.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_back_fab))
                bottom_app_bar.replaceMenu(R.menu.secondary_menu)
            } else {
                isMain = true
                bottom_app_bar.navigationIcon =
                    ContextCompat.getDrawable(context!!, R.drawable.ic_hamburger_menu_bottom_bar)
                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                fab.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_plus_fab))
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

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
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
                    bottom_sheet_description_header.text = serverResponseData.title
                    bottom_sheet_description.text = serverResponseData.explanation
                }
            }

            is PictureOfTheDayData.Loading -> {
                loadingLayout.visibility = View.VISIBLE
            }

            is PictureOfTheDayData.Error -> {
                loadingLayout.visibility = View.GONE
            }
        }
    }
}
