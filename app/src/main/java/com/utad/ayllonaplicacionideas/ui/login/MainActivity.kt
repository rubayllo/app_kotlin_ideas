package com.utad.ayllonaplicacionideas.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.utad.ayllonaplicacionideas.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private val binding: ActivityMainBinding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}