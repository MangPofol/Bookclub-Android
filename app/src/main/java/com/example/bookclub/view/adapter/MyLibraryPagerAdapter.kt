package com.example.bookclub.view.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bookclub.view.library.BookListFragment

class MyLibraryPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {
    private val NUM_PAGES = 3
    private val nowBookListFragment: BookListFragment = BookListFragment()
    private val afterBookListFragment: BookListFragment = BookListFragment()
    private val beforeBookListFragment: BookListFragment = BookListFragment()

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> nowBookListFragment
            1 -> afterBookListFragment
            else -> beforeBookListFragment
        }
    }

    fun getAdapter(position: Int): BookAdapter {
        return when (position) {
            0 -> nowBookListFragment.getAdapter()
            1 -> afterBookListFragment.getAdapter()
            else -> beforeBookListFragment.getAdapter()
        }
    }
}