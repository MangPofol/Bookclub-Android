package com.mangpo.bookclub.view.library

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentMyLibraryBinding
import com.mangpo.bookclub.view.setting.SettingActivity
import com.mangpo.bookclub.view.adapter.MyLibraryPagerAdapter
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.BookViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyLibraryFragment : Fragment(), TextWatcher {
    private lateinit var binding: FragmentMyLibraryBinding
    private lateinit var myLibraryPagerAdapter: MyLibraryPagerAdapter
    //private lateinit var bookClubFilterAdapter: BookClubFilterAdapter //베타 버전 출시 후 사용

    private val bookVm: BookViewModel by sharedViewModel()
    private val fragments: List<BookListFragment> =
        listOf(BookListFragment("NOW"), BookListFragment("AFTER"), BookListFragment("BEFORE"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MyLibraryFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("MyLibraryFragment", "onCreateView")
        binding = FragmentMyLibraryBinding.inflate(inflater, container, false)  //뷰바인딩 초기화

        myLibraryPagerAdapter = MyLibraryPagerAdapter(context as FragmentActivity, fragments)

        binding.viewPager.adapter = myLibraryPagerAdapter  //어댑터 설정

        //햄버거 아이콘 클릭 리스너 -> SettingActivity 화면으로 이동
        binding.hamburgerIb.setOnClickListener {
            startActivity(Intent(requireContext(), SettingActivity::class.java))
        }

        binding.searchLayout.searchBookET.addTextChangedListener(this)

        //읽는중, 완독, 읽고 싶은 페이지 변환 리스너
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                //모든 필터 초기화
                binding.searchButton.isChecked = false
                binding.sortButton.isChecked = false
                //키보드 올라와 있으면 키보드 내리기
                (activity as MainActivity).hideKeyBord(requireView())

                //완독 부분에선 북클럽 필터 GONE
                when (position) {
                    0 -> {
                        //setVisibilityClubButton(View.VISIBLE)
                        bookVm.setReadType("NOW")
                    }
                    1 -> {
                        //setVisibilityClubButton(View.VISIBLE)
                        bookVm.setReadType("AFTER")
                    }
                    2 -> {
                        //setVisibilityClubButton(View.GONE)
                        bookVm.setReadType("BEFORE")
                    }
                }
            }
        })

        //main 검색 버튼 클릭 리스너
        binding.searchButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                bookVm.setSearchFilterBtnClick(1)   //뷰모델에 검색 버튼이 클릭됐다는 것을 알려주기
                binding.clubButton.isChecked = false
                binding.sortButton.isChecked = false
                binding.searchLayout.root.visibility = View.VISIBLE
                binding.searchLayout.searchBookET.text.clear()
            } else {    //뷰모델에 검색 버튼이 클릭이 취소됐다는 것을 알려주기
                bookVm.setSearchFilterBtnClick(0)
                binding.searchLayout.root.visibility = View.GONE
                binding.searchLayout.searchBookET.text.clear()  //검색어 초기화
                (activity as MainActivity).hideKeyBord(requireView())   //키보드 숨기기
            }
        }

        //main 북클럽 필터 버튼 클릭 리스너 //베타 버전 출시 후 사용
        /*binding.clubButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.searchButton.isChecked = false
                binding.sortButton.isChecked = false

                if (clubViewModel.clubs.value!!.size != 0) {
                    binding.bookClubFilterLayout.root.visibility = View.VISIBLE
                    bookClubFilterAdapter = BookClubFilterAdapter(clubViewModel.clubs.value!!)
                    binding.bookClubFilterLayout.clubFilterRecyclerView.adapter =
                        bookClubFilterAdapter
                    binding.bookClubFilterLayout.clubFilterRecyclerView.addItemDecoration(
                        HorizontalItemDecorator(10)
                    )
                }
            } else {
                binding.bookClubFilterLayout.root.visibility = View.GONE
            }
        }*/

        //main 정렬 필터 버튼 클릭 리스너
        binding.sortButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {    //뷰모델에 정렬 필터가 클릭됐다는 것을 알려주기
                bookVm.setSortFilterBtnClick(1)
                binding.searchButton.isChecked = false
                binding.clubButton.isChecked = false
                binding.sortFilterLayout.root.visibility = View.VISIBLE
                binding.sortFilterLayout.latestOrder.isChecked = false
                binding.sortFilterLayout.oldOrder.isChecked = false
                binding.sortFilterLayout.nameOrder.isChecked = false
            } else {    //뷰모델에 정렬 필터 클릭이 취소됐다는 것을 알려주기
                bookVm.setSortFilterBtnClick(0)
                binding.sortFilterLayout.root.visibility = View.GONE
                binding.sortFilterLayout.latestOrder.isChecked = false
                binding.sortFilterLayout.oldOrder.isChecked = false
                binding.sortFilterLayout.nameOrder.isChecked = false
            }
        }

        //정렬-최신순 필터 체크 리스너
        binding.sortFilterLayout.latestOrder.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                binding.sortFilterLayout.oldOrder.isChecked = false
                binding.sortFilterLayout.nameOrder.isChecked = false
                bookVm.setMyLibrarySort("Latest")
            } else {
                bookVm.setMyLibrarySort("")
            }
        }

        //정렬-오래된순 필터 체크 리스너
        binding.sortFilterLayout.oldOrder.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                binding.sortFilterLayout.latestOrder.isChecked = false
                binding.sortFilterLayout.nameOrder.isChecked = false
                bookVm.setMyLibrarySort("Old")
            } else {
                bookVm.setMyLibrarySort("")
            }
        }

        //정렬-이름순 필터 체크 리스너
        binding.sortFilterLayout.nameOrder.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                binding.sortFilterLayout.latestOrder.isChecked = false
                binding.sortFilterLayout.oldOrder.isChecked = false
                bookVm.setMyLibrarySort("Name")
            } else {
                bookVm.setMyLibrarySort("")
            }
        }

        observe()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MyLibraryFragment", "onViewCreated")

        TabLayoutMediator(binding.readTypeTabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.reading)
                1 -> tab.text = getString(R.string.read_complete)
                2 -> tab.text = getString(R.string.want_to_read)
                else -> null
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("MyLibraryFragment", "onDestroyView")
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        bookVm.setMyLibrarySearch(s.toString())
    }

    override fun afterTextChanged(s: Editable?) {
    }

    //베타 버전 출시 후 사용
    /*private fun setVisibilityClubButton(visibility: Int) {
        binding.clubButton.visibility = visibility
    }*/

    private fun observe() {
        bookVm.readType.observe(viewLifecycleOwner, Observer {
            when (it) {
                "NOW" -> binding.viewPager.currentItem = 0
                "AFTER" -> binding.viewPager.currentItem = 1
                else -> binding.viewPager.currentItem = 2
            }
        })

        //검색 필터 버튼 클릭 Observer -> 정렬 필터도 클릭이 안 돼 있는 상황이면 세부 필터 클릭 레이아웃의 View=Gone 으로 변경한다.
        bookVm.searchFilterBtnClick.observe(viewLifecycleOwner, Observer {
            if (it == 0 && bookVm.sortFilterBtnClick.value == 0) {
                binding.filterContentsLayout.visibility = View.GONE
            } else {
                binding.filterContentsLayout.visibility = View.VISIBLE
            }
        })

        //정렬 필터 버튼 클릭 Observer -> 검색 필터도 클릭이 안 돼 있는 상황이면 세부 필터 클릭 레이아웃의 View=Gone 으로 변경한다.
        bookVm.sortFilterBtnClick.observe(viewLifecycleOwner, Observer {
            if (it == 0 && bookVm.searchFilterBtnClick.value == 0) {
                binding.filterContentsLayout.visibility = View.GONE
            } else {
                binding.filterContentsLayout.visibility = View.VISIBLE
            }
        })
    }

}

