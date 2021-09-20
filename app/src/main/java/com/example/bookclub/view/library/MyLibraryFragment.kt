package com.example.bookclub.view.library

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.bookclub.view.MainActivity
import com.example.bookclub.R
import com.example.bookclub.view.adapter.MyLibraryPagerAdapter
import com.example.bookclub.databinding.FragmentMyLibraryBinding
import com.example.bookclub.viewmodel.BookViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MyLibraryFragment : Fragment() {
    private lateinit var binding: FragmentMyLibraryBinding
    private val bookViewModel: BookViewModel by activityViewModels()
    private lateinit var myLibraryPagerAdapter: MyLibraryPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyLibraryBinding.inflate(inflater, container, false)  //뷰바인딩 초기화

        //메인 필터 체크박스 리스너
        val checkBoxListener = CompoundButton.OnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                changeFilterCheckBox(checkBox.id)
            } else {
                isNotCheckedAll()
            }
        }

        //메인 필터 체크박스 리스너 등록
        binding.searchButton.setOnCheckedChangeListener(checkBoxListener)
        binding.clubButton.setOnCheckedChangeListener(checkBoxListener)
        binding.sortButton.setOnCheckedChangeListener(checkBoxListener)

        myLibraryPagerAdapter = MyLibraryPagerAdapter(context as FragmentActivity)
        binding.viewPager.adapter = myLibraryPagerAdapter  //어댑터 설정

        //페이지 변환 후 호출되는 콜백 함수
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("ViewPagerFragment", "Page ${position + 1}")
//                viewPagerStack.add(position)

                binding.searchButton.isChecked = false
                binding.clubButton.isChecked = false
                binding.sortButton.isChecked = false

                bookViewModel.updateReadType(position)

                //완독 탭을 누르면 콜백 함수가 두 번 호출되는 버그 해결하기 위한 방법
                /* if (viewPagerStack.size > 1 && viewPagerStack[viewPagerStack.lastIndex - 1] != position) {
                     when (myLibraryViewModel.selectedFilter?.value) {
                         binding.searchButton.id -> binding.searchButton.isChecked = false
                         binding.clubButton.id -> binding.clubButton.isChecked = false
                         binding.sortButton.id -> binding.sortButton.isChecked = false
                     }
                 }*/

                //완독 부분에선 북클럽 필터 GONE
                when (position) {
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

        (activity as MainActivity).setDrawer(binding.toolbar)   //navigation drawer 등록
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_more_vert_36_black)  //navigation icon 설정

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

    //체크된 필터 말고는 모두 체크 해제 + 필터에 맞는 뷰 동적 생성
    private fun changeFilterCheckBox(checkBoxId: Int) {
        when (checkBoxId) {
            binding.searchButton.id -> {
                binding.clubButton.isChecked = false
                binding.sortButton.isChecked = false
                childFragmentManager.beginTransaction()
                    .replace(
                        binding.filterLayout.id,
                        SearchFragment(myLibraryPagerAdapter.getAdapter(bookViewModel.readType.value!!))
                    ).commit()
            }
            binding.clubButton.id -> {
                binding.searchButton.isChecked = false
                binding.sortButton.isChecked = false
                childFragmentManager.beginTransaction()
                    .replace(binding.filterLayout.id, BookClubFilterFragment()).commit()
            }
            binding.sortButton.id -> {
                binding.searchButton.isChecked = false
                binding.clubButton.isChecked = false
                childFragmentManager.beginTransaction()
                    .replace(binding.filterLayout.id, SortFilterFragment()).commit()
            }
        }
    }

    //세 필터 모두 체크 해제됐는지 확인
    private fun isNotCheckedAll() {
        if (!binding.searchButton.isChecked && !binding.clubButton.isChecked && !binding.sortButton.isChecked) {
            binding.filterLayout.removeAllViewsInLayout()
        }
    }

}

