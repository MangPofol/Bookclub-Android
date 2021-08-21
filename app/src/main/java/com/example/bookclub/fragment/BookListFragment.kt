package com.example.bookclub.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookclub.MainActivity
import com.example.bookclub.R
import com.example.bookclub.adapter.BookAdapter
import com.example.bookclub.databinding.FragmentBookListBinding
import com.example.bookclub.util.HorizontalItemDecorator
import com.example.bookclub.util.VerticalItemDecorator
import com.example.bookclub.viewmodel.MainFilterViewModel

class BookListFragment: Fragment() {
    private lateinit var binding: FragmentBookListBinding
    private lateinit var mainFilterViewModel: MainFilterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookListBinding.inflate(inflater, container, false)  //뷰바인딩 초기화
        mainFilterViewModel = ViewModelProvider(activity as MainActivity)[MainFilterViewModel::class.java]  //뷰모델 초기화

        binding.bookListRecyclerView.adapter = BookAdapter()    //어댑터 설정
        binding.bookListRecyclerView.layoutManager = GridLayoutManager(this.context, 3) //레이아웃 설정
        //아이템 간 간격 설정
        binding.bookListRecyclerView.addItemDecoration(VerticalItemDecorator(50))
        binding.bookListRecyclerView.addItemDecoration(HorizontalItemDecorator(20))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //메인 필터 클릭 이벤트 observer
        mainFilterViewModel.selectedFilter.observe(activity as MainActivity, Observer { mainFilter ->
            binding.filterLayout.visibility = View.VISIBLE

            when(mainFilter) {
                R.id.searchButton -> {
                    childFragmentManager.beginTransaction().replace(binding.filterLayout.id, SearchFragment()).commit()
                }
                R.id.clubButton -> {
                    childFragmentManager.beginTransaction().replace(binding.filterLayout.id, BookClubFilterFragment()).commit()
                }
                R.id.sortButton -> {
                    childFragmentManager.beginTransaction().replace(binding.filterLayout.id, SortFilterFragment()).commit()
                }
            }
        })
    }
}