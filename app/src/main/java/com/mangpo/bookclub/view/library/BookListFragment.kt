package com.mangpo.bookclub.view.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.mangpo.bookclub.databinding.FragmentBookListBinding
import com.mangpo.bookclub.util.HorizontalItemDecorator
import com.mangpo.bookclub.util.VerticalItemDecorator
import com.mangpo.bookclub.view.adapter.BookAdapter

class BookListFragment : Fragment() {
    private lateinit var binding: FragmentBookListBinding
    private val bookAdapter: BookAdapter = BookAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookListBinding.inflate(inflater, container, false)  //뷰바인딩 초기화

        binding.bookListRecyclerView.adapter = bookAdapter    //어댑터 설정
        binding.bookListRecyclerView.layoutManager = GridLayoutManager(this.context, 3) //레이아웃 설정
        //아이템 간 간격 설정
        binding.bookListRecyclerView.addItemDecoration(VerticalItemDecorator(40))
        binding.bookListRecyclerView.addItemDecoration(HorizontalItemDecorator(20))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun getAdapter(): BookAdapter {
        return bookAdapter
    }
}