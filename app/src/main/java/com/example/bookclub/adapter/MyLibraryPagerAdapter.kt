package com.example.bookclub.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bookclub.fragment.CompleteReadingFragment
import com.example.bookclub.fragment.ReadingFragment
import com.example.bookclub.fragment.WantReadFragment

class MyLibraryPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val READING = 0
    private val COMPLETE_READING = 1
    private val WANT_TO_READ = 2

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            READING -> ReadingFragment()
            COMPLETE_READING -> CompleteReadingFragment()
            else -> WantReadFragment()
        }
    }
}