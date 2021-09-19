package com.example.bookclub.view.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.bookclub.view.bookclub.BookClubFragment
import com.example.bookclub.view.library.MyLibraryFragment
import com.example.bookclub.view.write.RecordFragment
import com.example.bookclub.view.write.SelectBookFragment
import com.example.bookclub.view.write.WriteFragment

class BottomNavigationPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        Log.e("getItem", position.toString())
        return when (position) {
            0 -> WriteFragment()
            1 -> MyLibraryFragment()
            else -> BookClubFragment()
        }
    }

}