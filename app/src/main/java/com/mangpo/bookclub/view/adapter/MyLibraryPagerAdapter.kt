package com.mangpo.bookclub.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mangpo.bookclub.view.library.BookListFragment

class MyLibraryPagerAdapter(fa: FragmentActivity, val fragments: List<BookListFragment>): FragmentStateAdapter(fa) {
    private val NUM_PAGES = 3

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment = fragments[position]

}