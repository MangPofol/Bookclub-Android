package com.example.bookclub.view.library

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookclub.view.MainActivity
import com.example.bookclub.R
import com.example.bookclub.view.adapter.BookAdapter
import com.example.bookclub.databinding.FragmentBookListBinding
import com.example.bookclub.util.HorizontalItemDecorator
import com.example.bookclub.util.VerticalItemDecorator
import com.example.bookclub.viewmodel.MyLibraryViewModel

class BookListFragment : Fragment() {
    private lateinit var binding: FragmentBookListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookListBinding.inflate(inflater, container, false)  //뷰바인딩 초기화

        binding.bookListRecyclerView.adapter = BookAdapter()    //어댑터 설정
        binding.bookListRecyclerView.layoutManager = GridLayoutManager(this.context, 3) //레이아웃 설정
        //아이템 간 간격 설정
        binding.bookListRecyclerView.addItemDecoration(VerticalItemDecorator(40))
        binding.bookListRecyclerView.addItemDecoration(HorizontalItemDecorator(20))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}