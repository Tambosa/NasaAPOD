package com.aroman.nasaapod

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aroman.nasaapod.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val prefs: SharedPreferences by lazy {
        getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        when (prefs.getBoolean("THEME", true)) {
            true -> setTheme(R.style.Theme_NasaAPOD)
            false -> setTheme(R.style.CustomNasaAPOD)
        }

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, MainFragment.newInstance())
            .commit()
    }
}