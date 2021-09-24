package com.mangpo.bookclub.view.library

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentMyLibraryBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.view.MainActivity
import com.mangpo.bookclub.view.adapter.BookAdapter
import com.mangpo.bookclub.view.adapter.MyLibraryPagerAdapter
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.MyLibraryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyLibraryFragment : Fragment() {
    private lateinit var binding: FragmentMyLibraryBinding
    private lateinit var myLibraryPagerAdapter: MyLibraryPagerAdapter

    private val myLibraryViewModel: MyLibraryViewModel by activityViewModels<MyLibraryViewModel>()
    private val bookViewModel: BookViewModel by activityViewModels<BookViewModel>()

    private var books: MutableList<BookModel> = ArrayList<BookModel>()
    private var adapter: BookAdapter = BookAdapter()

    /*init {
        *//*CoroutineScope(Dispatchers.Main).launch {
            bookViewModel.getBooks("NOW")!!
            bookViewModel.getBooks("AFTER")!!
            bookViewModel.getBooks("BEFORE")!!
        }*//*
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyLibraryBinding.inflate(inflater, container, false)  //뷰바인딩 초기화

        myLibraryPagerAdapter = MyLibraryPagerAdapter(context as FragmentActivity)
        binding.viewPager.adapter = myLibraryPagerAdapter  //어댑터 설정

        //페이지 변환 후 호출되는 콜백 함수
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("ViewPagerFragment", "Page ${position + 1}")
                myLibraryViewModel.updateLibraryReadType(position)

                //완독 부분에선 북클럽 필터 GONE
                when (position) {
                    0 -> setVisibilityClubButton(View.VISIBLE)
                    1 -> setVisibilityClubButton(View.VISIBLE)
                    2 -> setVisibilityClubButton(View.GONE)
                }
            }
        })

        binding.searchButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                myLibraryViewModel.updateMainFilter(0)
            } else {
                isAllNotChecked()
            }
        }

        binding.clubButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                myLibraryViewModel.updateMainFilter(1)
            } else {
                isAllNotChecked()
            }
        }

        binding.sortButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                myLibraryViewModel.updateMainFilter(2)
            } else {
                isAllNotChecked()
            }
        }

        //읽는중, 완독, 읽고싶은 observe
        myLibraryViewModel.libraryReadType.observe(viewLifecycleOwner, Observer {
            Log.e("libraryReadType observe", it.toString())
            myLibraryViewModel.updateMainFilter(-1)
            myLibraryViewModel.updateSortFilter(-1)

            adapter = myLibraryPagerAdapter.getAdapter(it)

            when (it) {
                0 ->CoroutineScope(Dispatchers.Main).launch {
                        books = bookViewModel.getBooks("NOW")!!
                        adapter.setBooks(books)
                    }
                1 -> CoroutineScope(Dispatchers.Main).launch {
                    books = bookViewModel.getBooks("AFTER")!!
                    adapter.setBooks(books)
                }
                2 -> CoroutineScope(Dispatchers.Main).launch {
                    books = bookViewModel.getBooks("BEFORE")!!
                    adapter.setBooks(books)
                }
            }
            adapter.setBooks(books)
        })

        //mainFilter: 검색, 북클럽, 정렬 observe
        myLibraryViewModel.mainFilter.observe(viewLifecycleOwner, Observer {
            Log.e("mainFilter observe", it.toString())
            adapter.setBooks(books)

            when(it) {
                0 -> {
                    binding.viewPager.setPadding(0, 23, 0, 0)

                    binding.filterLayout.visibility = View.VISIBLE
                    binding.clubButton.isChecked = false
                    binding.sortButton.isChecked = false

                    childFragmentManager.beginTransaction().replace(binding.filterLayout.id, SearchFragment(adapter)).commit()
                }
                1 -> {
                    binding.viewPager.setPadding(0, 26, 0, 0)

                    binding.filterLayout.visibility = View.VISIBLE
                    binding.searchButton.isChecked = false
                    binding.sortButton.isChecked = false

                    childFragmentManager.beginTransaction().replace(binding.filterLayout.id, BookClubFilterFragment()).commit()
                }
                2 -> {
                    binding.viewPager.setPadding(0, 26, 0, 0)

                    binding.filterLayout.visibility = View.VISIBLE
                    binding.searchButton.isChecked = false
                    binding.clubButton.isChecked = false

                    childFragmentManager.beginTransaction().replace(binding.filterLayout.id, SortFilterFragment()).commit()
                }
                -1 -> {
                    binding.searchButton.isChecked = false
                    binding.clubButton.isChecked = false
                    binding.sortButton.isChecked = false
                    binding.filterLayout.removeAllViews()

                }
            }
        })

        //정렬 필터: 최신순, 오래된순, 이름순 observe
        myLibraryViewModel.sortFilter.observe(viewLifecycleOwner, Observer { sortFilter ->
            Log.e("sortFilter observe", sortFilter.toString())

            if (books.size!=0) {
                when (sortFilter) {
                    0 -> {

                    }
                    1 -> {

                    }
                    2 -> adapter.setBooks(books.sortedWith(compareBy { it.name }) as MutableList<BookModel>)
                    -1 -> adapter.setBooks(books)
                }
            } else {
                Log.e("MyLibrary sortFilter observe", "등록된 책이 없습니다.")
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

    private fun isAllNotChecked() {
        if (!binding.searchButton.isChecked&&!binding.clubButton.isChecked&&!binding.sortButton.isChecked)
            myLibraryViewModel.updateMainFilter(-1)
    }

}

