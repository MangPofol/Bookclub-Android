package com.example.bookclub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bookclub.adapter.BottomNavigationPagerAdapter
import com.example.bookclub.databinding.ActivityMainBinding
import com.example.bookclub.fragment.BookListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomViewPager.adapter = BottomNavigationPagerAdapter(supportFragmentManager)
        binding.bottomViewPager.currentItem = 1
        binding.bottomNavigation.selectedItemId = R.id.library

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.write -> {
                    binding.bottomViewPager.currentItem = 0

                    return@setOnItemSelectedListener true
                }
                R.id.library -> {
                    binding.bottomViewPager.currentItem = 1

                    return@setOnItemSelectedListener true
                }
                R.id.myBookclub -> {
                    binding.bottomViewPager.currentItem = 2

                    return@setOnItemSelectedListener true
                }
            }

            return@setOnItemSelectedListener false
        }
    }
}