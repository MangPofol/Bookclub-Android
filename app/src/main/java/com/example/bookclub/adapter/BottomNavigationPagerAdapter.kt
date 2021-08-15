package com.example.bookclub.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.bookclub.fragment.MyLibraryFragment
import com.example.bookclub.fragment.WriteFragment

class BottomNavigationPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> WriteFragment()
            1 -> MyLibraryFragment()
            else -> MyLibraryFragment()
        }
    }

}