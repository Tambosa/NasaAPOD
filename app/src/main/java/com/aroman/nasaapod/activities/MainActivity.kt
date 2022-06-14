package com.aroman.nasaapod.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aroman.nasaapod.fragments.MainFragment
import com.aroman.nasaapod.R

class MainActivity : AppCompatActivity() {

    private val prefs: SharedPreferences by lazy {
        getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        when (prefs.getBoolean("THEME", true)) {
            true -> setTheme(R.style.Theme_NasaAPOD)
            false -> setTheme(R.style.CustomNasaAPOD)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, MainFragment.newInstance())
            .commit()
    }
}