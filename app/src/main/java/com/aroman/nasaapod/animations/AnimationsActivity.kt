package com.aroman.nasaapod.animations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import com.aroman.nasaapod.R
import kotlinx.android.synthetic.main.activity_animations.*

class AnimationsActivity : AppCompatActivity() {

    private var textIsVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animations)

        button.setOnClickListener{
            TransitionManager.beginDelayedTransition(transitions_container)
            textIsVisible = !textIsVisible
            text.visibility = if (textIsVisible) View.VISIBLE else View.GONE
        }
    }
}