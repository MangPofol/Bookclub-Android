package com.mangpo.bookclub.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mangpo.bookclub.view.main.AlarmFragment
import com.mangpo.bookclub.view.main.MyBookClubFragment
import com.mangpo.bookclub.view.main.SettingFragment

class NavigationViewViewpagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> AlarmFragment()
            1 -> MyBookClubFragment()
            else -> SettingFragment()
        }
    }
}