package com.example.bookclub.view.write

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookclub.databinding.FragmentSelectBookBinding
import com.example.bookclub.util.HorizontalItemDecorator
import com.example.bookclub.util.VerticalItemDecorator
import com.example.bookclub.view.MainActivity
import com.example.bookclub.view.adapter.BookAdapter
import com.example.bookclub.view.adapter.OnBookItemClick
import com.example.bookclub.viewmodel.BookViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SelectBookFragment : Fragment(), TextWatcher, OnBookItemClick {
    private lateinit var binding: FragmentSelectBookBinding
    private lateinit var bookAdapter: BookAdapter
    private val bookViewModel: BookViewModel by activityViewModels()
    private val bottomSheet: AddBookBottomSheetFragment = AddBookBottomSheetFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookAdapter = BookAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectBookBinding.inflate(inflater, container, false)

        bookViewModel.books.observe(viewLifecycleOwner, Observer {
            Log.e("observe!!", it.toString())
            bookAdapter.setBooks(it)
        })

        //책 검색 EditText에 TextChanged 리스너 등록
        binding.searchBookET.addTextChangedListener(this)

        binding.readTypeRG.setOnCheckedChangeListener { group, checkedId ->

        }

        //Recycler View 어댑터 설정
        binding.bookListRV.adapter = bookAdapter
        binding.bookListRV.layoutManager = GridLayoutManager(this.context, 3)
        binding.bookListRV.addItemDecoration(VerticalItemDecorator(50))
        binding.bookListRV.addItemDecoration(HorizontalItemDecorator(20))

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        (parentFragment as WriteFragment).changeChildFragment(0)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        GlobalScope.launch {
            bookViewModel.updateSearchBookTitle(s.toString())
        }

        if (s!!.isNotEmpty()) { //검색 테스트가 안 비어 있으면 recyclerview -> VISIBLE, radio group -> INVISIBLE
            binding.selectBookTV.visibility = View.INVISIBLE
            binding.readTypeRG.visibility = View.INVISIBLE
            binding.bookListRV.visibility = View.VISIBLE
        } else {    //검색 테스트가 비어 있으면 recyclerview -> INVISIBLE, radio group -> VISIBLE
            binding.selectBookTV.visibility = View.VISIBLE
            binding.readTypeRG.visibility = View.VISIBLE
            binding.bookListRV.visibility = View.INVISIBLE
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun onClick(title: String) {
        //책을 등록하는 bottom sheet 을 띄운다.
        bottomSheet.show(
            (activity as MainActivity).supportFragmentManager, bottomSheet.tag
        )
        //클릭된 책 제목을 SelectBookViewModel의 selectedBookTitle 변수에 업데이트
        bookViewModel.updateSelectedTookTitle(title)
    }
}