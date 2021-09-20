package com.example.bookclub.view.library

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookclub.view.adapter.BookAdapter
import com.example.bookclub.databinding.FragmentBookListBinding
import com.example.bookclub.util.HorizontalItemDecorator
import com.example.bookclub.util.VerticalItemDecorator
import com.example.bookclub.view.MainActivity
import com.example.bookclub.viewmodel.BookViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BookListFragment : Fragment() {
    private lateinit var binding: FragmentBookListBinding
    private lateinit var bookAdapter: BookAdapter
    private val bookViewModel: BookViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bookAdapter = BookAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookListBinding.inflate(inflater, container, false)  //뷰바인딩 초기화

        CoroutineScope(Dispatchers.Main).launch {
            bookViewModel.getBooks("NOW")
            bookViewModel.getBooks("AFTER")
            bookViewModel.getBooks("BEFORE")

            bookViewModel.readType.observe(viewLifecycleOwner, Observer {
                when (it) {
                    0 -> bookAdapter.setBooks(bookViewModel.nowBooks.value!!)
                    1 -> bookAdapter.setBooks(bookViewModel.afterBooks.value!!)
                    2 -> bookAdapter.setBooks(bookViewModel.beforeBooks.value!!)
                }

            })
        }

        bookViewModel.nowBooks.observe(viewLifecycleOwner, Observer {
            if (bookViewModel.readType.value==0)
                bookAdapter.setBooks(it)
        })

        bookViewModel.afterBooks.observe(viewLifecycleOwner, Observer {
            if (bookViewModel.readType.value==1)
                bookAdapter.setBooks(it)
        })

        bookViewModel.beforeBooks.observe(viewLifecycleOwner, Observer {
            if (bookViewModel.readType.value==2)
                bookAdapter.setBooks(it)
        })

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