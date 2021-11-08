package com.mangpo.bookclub.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mangpo.bookclub.model.ClubModel
import com.mangpo.bookclub.view.bookclub.BookClubFragment
import com.mangpo.bookclub.view.library.MyLibraryFragment
import com.mangpo.bookclub.view.write.WriteFragment

class BottomNavigationPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentManager: FragmentManager = fm
    private val writeFragment: WriteFragment = WriteFragment()
    private val myLibraryFragment: MyLibraryFragment = MyLibraryFragment()
    private val bookClubFragment: BookClubFragment = BookClubFragment()

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> writeFragment
            1 -> myLibraryFragment
            else -> bookClubFragment
        }
    }

    /*fun sendNewClub(newClub: ClubModel) {
        bookClubFragment.addClub(newClub)
    }*/

}