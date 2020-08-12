package dev.leonlatsch.photok.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.leonlatsch.photok.R
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        mainNavHostFragment.findNavController()
            .addOnDestinationChangedListener {_, destination, _ ->
                when(destination.id) {
                    R.id.galleryFragment, R.id.settingsFragment -> appBarLayout.visibility = View.VISIBLE
                    else -> appBarLayout.visibility = View.GONE
                }
            }
    }
}