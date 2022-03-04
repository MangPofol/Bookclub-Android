package com.mangpo.bookclub.view.adpater

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mangpo.bookclub.view.main.library.BookListFragment

class LibraryFragmentAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    private val fragmentList = listOf<Fragment>(BookListFragment("NOW"), BookListFragment("AFTER"), BookListFragment("BEFORE"))

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}