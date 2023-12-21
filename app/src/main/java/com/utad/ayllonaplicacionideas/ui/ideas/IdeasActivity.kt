package com.utad.ayllonaplicacionideas.ui.ideas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.utad.ayllonaplicacionideas.R
import com.utad.ayllonaplicacionideas.databinding.ActivityIdeasBinding

class IdeasActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityIdeasBinding
    private val binding: ActivityIdeasBinding get() = _binding

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityIdeasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtonNavigation()
    }

    private fun setButtonNavigation() {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(binding.nhfIdeas.id) as NavHostFragment
            navController = navHostFragment.findNavController()

        if (navHostFragment != null) {
            binding.bottomNavigationView.setupWithNavController(navController)
        }
    }
}