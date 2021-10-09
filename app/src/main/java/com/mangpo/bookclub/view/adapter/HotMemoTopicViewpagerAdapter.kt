package com.mangpo.bookclub.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mangpo.bookclub.view.bookclub.HotMemoTopicFragment

class HotMemoTopicViewpagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HotMemoTopicFragment("MEMO")
            else -> HotMemoTopicFragment("TOPIC")
        }
    }
}