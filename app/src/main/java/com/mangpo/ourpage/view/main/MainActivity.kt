package com.mangpo.ourpage.view.main

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mangpo.ourpage.databinding.ActivityMainBinding
import com.mangpo.ourpage.view.BaseActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mangpo.ourpage.R

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.main_fragment_container
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.main_bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun initAfterBinding() {
    }

    override fun onBackPressed() {
        if (onBackPressedDispatcher.hasEnabledCallbacks()) {
            onBackPressedDispatcher.onBackPressed()
            return
        }
        super.onBackPressed()
    }
}