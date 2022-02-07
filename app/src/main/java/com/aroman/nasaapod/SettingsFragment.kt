package com.aroman.nasaapod

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*

class SettingsFragment : Fragment() {

    private val prefs: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (prefs.getBoolean("THEME", true)) {
            true -> default_theme_chip.isChecked = true
            false -> custom_theme_chip.isChecked = true
        }

        theme_chip_group.setOnCheckedChangeListener { _, checkedID: Int ->
            when (checkedID) {
                default_theme_chip.id -> {
                    prefs.edit().putBoolean("THEME", true).apply()
                    requireActivity().apply {
                        setTheme(R.style.Theme_NasaAPOD)
                    }
                }
                custom_theme_chip.id -> {
                    prefs.edit().putBoolean("THEME", false).apply()
                    requireActivity().apply {
                        setTheme(R.style.CustomNasaAPOD)
                    }
                }
            }
        }
    }
}