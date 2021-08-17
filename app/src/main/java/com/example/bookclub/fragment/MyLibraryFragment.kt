package com.example.bookclub.fragment

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.bookclub.R
import com.example.bookclub.adapter.MyLibraryPagerAdapter
import com.example.bookclub.databinding.FragmentMyLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class MyLibraryFragment : Fragment() {
    private lateinit var binding: FragmentMyLibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyLibraryBinding.inflate(inflater, container, false)

        //viewPager adapter 설정
        binding.viewPager.adapter = MyLibraryPagerAdapter(this)

        //tablayout이랑 viewPager 연결
        TabLayoutMediator(binding.readTypeTabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.reading)
                1 -> tab.text = getString(R.string.read_complete)
                else -> tab.text = getString(R.string.want_to_read)
            }
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int){
                super.onPageSelected(position)
                when(position) {
                    0 -> setVisibilityClubButton(View.VISIBLE)
                    1 -> setVisibilityClubButton(View.VISIBLE)
                    2 -> setVisibilityClubButton(View.GONE)
                }
            }
        })

        //val sp: Float = 10 / resources.displayMetrics.scaledDensity
        //val dp: Float = 5 / resources.displayMetrics.density
        //Log.d("dp", dp.toString())

        return binding.root
    }

    private fun setVisibilityClubButton(visibility: Int) {
        binding.clubButton.visibility = visibility
    }
}