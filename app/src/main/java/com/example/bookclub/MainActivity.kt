package com.example.bookclub

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.bookclub.adapter.BottomNavigationPagerAdapter
import com.example.bookclub.databinding.ActivityMainBinding
import com.example.bookclub.fragment.BookListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mDrawerToggle: ActionBarDrawerToggle

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

    fun setDrawer(toolbar: Toolbar) {
        var activity = MainActivity()

        mDrawerToggle = ActionBarDrawerToggle(activity, binding.drawerLayout, toolbar, R.string.open, R.string.close)
        mDrawerToggle!!.syncState()
    }

    override fun onBackPressed() {
        //drawer layout이 열려 있는 상태면 -> drawer layout 부터 닫는다.
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawers()
        else {
            super.onBackPressed()
        }

    }
}