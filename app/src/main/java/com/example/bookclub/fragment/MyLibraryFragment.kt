package com.example.bookclub.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.CompoundButton
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.bookclub.MainActivity
import com.example.bookclub.R
import com.example.bookclub.adapter.MyLibraryPagerAdapter
import com.example.bookclub.viewmodel.MainFilterViewModel
import com.example.bookclub.databinding.FragmentMyLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator
import androidx.transition.Transition
import androidx.transition.Fade
import androidx.transition.TransitionManager


class MyLibraryFragment : Fragment() {
    private lateinit var binding: FragmentMyLibraryBinding
    private lateinit var mainFilterViewModel: MainFilterViewModel
    private var viewPagerStack: MutableList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyLibraryBinding.inflate(inflater, container, false)  //뷰바인딩 초기화
        mainFilterViewModel =
            ViewModelProvider(activity as MainActivity)[MainFilterViewModel::class.java]  //메인 필터 뷰모델 초기화

        //메인 필터 체크박스 리스너
        val checkBoxListener = CompoundButton.OnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                changeFilterCheckBox(checkBox.id)
            } else {
                isNotCheckedAll()
            }
        }

        //클럽을 선택하는 top sheet 화면을 여는 아이콘을 눌렀을 때 발생하는 이벤트 리스너
        binding.toolbar.setOnMenuItemClickListener {
            if (binding.clubListLayout.visibility==View.GONE) {
                val anim = AnimationUtils.loadAnimation(context, R.anim.slide_down)
                binding.clubListLayout.animation = anim
                binding.clubListLayout.visibility = View.VISIBLE
                binding.coordinatorLayout.isEnabled = false
            } else {
                closeClubList()
            }

            true
        }

        //메인 필터 체크박스 리스너 등록
        binding.searchButton.setOnCheckedChangeListener(checkBoxListener)
        binding.clubButton.setOnCheckedChangeListener(checkBoxListener)
        binding.sortButton.setOnCheckedChangeListener(checkBoxListener)

        binding.viewPager.adapter = MyLibraryPagerAdapter(context as FragmentActivity)  //어댑터 설정

        //페이지 변환 후 호출되는 콜백 함수
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("ViewPagerFragment", "Page ${position + 1}")
                viewPagerStack.add(position)

                //완독 탭을 누르면 콜백 함수가 두 번 호출되는 버그 해결하기 위한 방법
                if (viewPagerStack.size > 1 && viewPagerStack[viewPagerStack.lastIndex - 1] != position) {
                    when (mainFilterViewModel.selectedFilter.value) {
                        binding.searchButton.id -> binding.searchButton.isChecked = false
                        binding.clubButton.id -> binding.clubButton.isChecked = false
                        binding.sortButton.id -> binding.sortButton.isChecked = false
                    }
                }

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
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_more_vert_36)  //navigation icon 설정

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

    //체크된 필터 말고는 모두 체크 해제
    private fun changeFilterCheckBox(checkBoxId: Int) {
        mainFilterViewModel.selectedFilter.value = checkBoxId

        when (checkBoxId) {
            binding.searchButton.id -> {
                binding.clubButton.isChecked = false
                binding.sortButton.isChecked = false
            }
            binding.clubButton.id -> {
                binding.searchButton.isChecked = false
                binding.sortButton.isChecked = false
            }
            binding.sortButton.id -> {
                binding.searchButton.isChecked = false
                binding.clubButton.isChecked = false
            }
        }
    }

    //세 필터 모두 체크 해제됐는지 확인
    private fun isNotCheckedAll() {
        if (!binding.searchButton.isChecked && !binding.clubButton.isChecked && !binding.sortButton.isChecked) {
            mainFilterViewModel.selectedFilter.value = null
        }
    }

    fun isClubListVisible(): Boolean {
        return binding.clubListLayout.visibility==View.VISIBLE
    }

    fun closeClubList() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        binding.clubListLayout.animation = anim
        binding.clubListLayout.visibility = View.GONE
    }

}

