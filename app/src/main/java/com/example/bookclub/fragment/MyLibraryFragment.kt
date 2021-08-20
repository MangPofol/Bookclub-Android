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
    private lateinit var myLibraryPagerAdapter: MyLibraryPagerAdapter

    private var searchFlag = 0
    private var clubFlag = 0
    private var sortFlag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyLibraryBinding.inflate(inflater, container, false)

        //viewPager adapter 설정
        myLibraryPagerAdapter = MyLibraryPagerAdapter()
        binding.viewPager.adapter = myLibraryPagerAdapter

        //tablayout이랑 viewPager 연결
        TabLayoutMediator(binding.readTypeTabLayout, binding.viewPager) { tab, position ->
            Log.d("position", position.toString())
            when (position) {
                0 -> tab.text = getString(R.string.reading)
                1 -> tab.text = getString(R.string.read_complete)
                2 -> tab.text = getString(R.string.want_to_read)
                else -> null
            }
        }.attach()

        // Paging 완료되면 호출
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int){
                super.onPageSelected(position)

                filterLayoutInit()
                when(position) {    //읽고 싶은 탭에서는 북클럽 필터 버튼 GONE 으로
                    0 -> setVisibilityClubButton(View.VISIBLE)
                    1 -> setVisibilityClubButton(View.VISIBLE)
                    2 -> setVisibilityClubButton(View.GONE)
                }
            }
        })

        binding.searchButton.setOnClickListener {
            if (searchFlag==0) {
                searchFlag = 1
                clubFlag = 0
                sortFlag = 0
                fragmentManager!!.beginTransaction().replace(binding.filterLayout.id, SearchFragment()).commit()
                binding.filterLayout.visibility = View.VISIBLE
            } else {
                searchFlag = 0
                binding.filterRadioGroup.clearCheck()
                binding.filterLayout.visibility = View.GONE
            }
            //Log.d("현재 플래그", "검색: ${searchFlag}, 북클럽: ${clubFlag}, 정렬: ${sortFlag}")
        }

        binding.clubButton.setOnClickListener {
            if (clubFlag==0) {
                searchFlag = 0
                clubFlag = 1
                sortFlag = 0
                fragmentManager!!.beginTransaction().replace(binding.filterLayout.id, BookClubFilterFragment()).commit()
                binding.filterLayout.visibility = View.VISIBLE
            } else {
                clubFlag = 0
                binding.filterRadioGroup.clearCheck()
                binding.filterLayout.visibility = View.GONE
            }
        }

        binding.sortButton.setOnClickListener {
            if (sortFlag==0) {
                searchFlag = 0
                clubFlag = 0
                sortFlag = 1
                fragmentManager!!.beginTransaction().replace(binding.filterLayout.id, SortFilterFragment()).commit()
                binding.filterLayout.visibility = View.VISIBLE
            } else {
                sortFlag = 0
                binding.filterRadioGroup.clearCheck()
                binding.filterLayout.visibility = View.GONE
            }
        }

        //val sp: Float = 10 / resources.displayMetrics.scaledDensity
//        val dp: Float = 3 / resources.displayMetrics.density
//        Log.d("dp", dp.toString())

        return binding.root
    }

    private fun filterLayoutInit() {
        searchFlag = 0
        clubFlag = 0
        sortFlag = 0

        binding.filterLayout.removeAllViewsInLayout()
        binding.filterLayout.visibility = View.GONE
        binding.filterRadioGroup.clearCheck()
    }

    private fun setVisibilityClubButton(visibility: Int) {
        binding.clubButton.visibility = visibility
    }
}