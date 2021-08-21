package com.example.bookclub.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.bookclub.MainActivity
import com.example.bookclub.R
import com.example.bookclub.adapter.MyLibraryPagerAdapter
import com.example.bookclub.databinding.FragmentMyLibraryBinding
import com.example.bookclub.viewmodel.MainFilterViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MyLibraryFragment : Fragment() {
    private lateinit var binding: FragmentMyLibraryBinding
    private lateinit var mainFilterViewModel: MainFilterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyLibraryBinding.inflate(inflater, container, false)  //뷰바인딩 초기화
        mainFilterViewModel = ViewModelProvider(activity as MainActivity)[MainFilterViewModel::class.java]  //메인 필터 뷰모델 초기화

        binding.viewPager.adapter = MyLibraryPagerAdapter(context as FragmentActivity)  //어댑터 설정

        //메인 필터 클릭 이벤트
        binding.filterRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            mainFilterViewModel.selectedFilter.value = checkedId
        }

        //페이지 변환 후 호출되는 콜백 함수
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("ViewPagerFragment", "Page ${position+1}")

                //완독 부분에선 북클럽 필터 GONE
                when(position) {
                    0 -> setVisibilityClubButton(View.VISIBLE)
                    1 -> setVisibilityClubButton(View.VISIBLE)
                    2 -> setVisibilityClubButton(View.GONE)
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TabLayoutMediator(binding.readTypeTabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.reading)
                1 -> tab.text = getString(R.string.read_complete)
                2 -> tab.text = getString(R.string.want_to_read)
                else -> null
            }
        }.attach()
    }

    private fun setVisibilityClubButton(visibility: Int) {
        binding.clubButton.visibility = visibility
    }

}

