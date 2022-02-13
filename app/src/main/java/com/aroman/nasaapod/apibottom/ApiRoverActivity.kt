package com.aroman.nasaapod.apibottom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.aroman.nasaapod.R
import com.aroman.nasaapod.api.CuriosityFragment
import com.aroman.nasaapod.api.OpportunityFragment
import com.aroman.nasaapod.api.SpiritFragment
import kotlinx.android.synthetic.main.activity_api_rover.*

class ApiRoverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_rover)

        bottom_navigation_view.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_view_curiosity -> initFragment(CuriosityFragment())
                R.id.bottom_view_opportunity -> initFragment(OpportunityFragment())
                R.id.bottom_view_spirit -> initFragment(SpiritFragment())
                else -> initFragment(CuriosityFragment())
            }
        }

        bottom_navigation_view.selectedItemId = R.id.bottom_view_curiosity
    }

    private fun initFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_api_bottom_container, fragment)
            .commitAllowingStateLoss()
        return true
    }
}