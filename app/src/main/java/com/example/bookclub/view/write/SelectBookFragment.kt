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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookAdapter = BookAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectBookBinding.inflate(inflater, container, false)

        //검색어가 변경되면 recycler view 의 책 목록이 계속 업데이트 됨.
        bookViewModel.searchedBooks.observe(viewLifecycleOwner, Observer {
            bookAdapter.setKakaoBooks(it)
        })

        //BookViewModel 의 readType 이 변경되면 recycler view 의 책 목록이 업데이트 된다.
        bookViewModel.readType.observe(viewLifecycleOwner, Observer {
            when (it) {
                0 -> bookAdapter.setBooks(bookViewModel.nowBooks.value!!)
                1 -> bookAdapter.setBooks(bookViewModel.afterBooks.value!!)
                2 -> bookAdapter.setBooks(bookViewModel.beforeBooks.value!!)
            }

        })

        //읽고 싶은 책 목록이 업데이트 되면(아마 읽고 싶은에 책이 추가됐을 때만 그럴 것 같음) recycler view 의 책 목록을 업데이트 한다.
        bookViewModel.beforeBooks.observe(viewLifecycleOwner, Observer {
            if (bookViewModel.readType.value == 2)
                bookAdapter.setBooks(it)
        })

        //책 검색 EditText에 TextChanged 리스너 등록
        binding.searchBookET.addTextChangedListener(this)

        //읽는중, 완독, 읽고 싶은 체크박스를 클릭하면 BookViewModel의 readType이 변경된다.
        binding.readTypeRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.readingRB.id -> bookViewModel.updateReadType(0)
                binding.readCompleteRB.id -> bookViewModel.updateReadType(1)
                binding.wantToReadRB.id -> bookViewModel.updateReadType(2)
            }
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
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        GlobalScope.launch {
            bookViewModel.updateSearchBookTitle(s.toString())
        }

        if (s!!.isNotEmpty()) { //검색 테스트가 안 비어 있으면 radio group -> INVISIBLE
            binding.selectBookTV.visibility = View.INVISIBLE
            binding.readTypeRG.visibility = View.INVISIBLE
        } else {    //검색 테스트가 비어 있으면 radio group -> VISIBLE, radio group을 읽고 있는이 체크돼 있도록, recycler view도 읽고 있는 책 목록으로 업데이트
            binding.selectBookTV.visibility = View.VISIBLE
            binding.readTypeRG.visibility = View.VISIBLE
            binding.readingRB.isChecked = true
            bookAdapter.setBooks(bookViewModel.nowBooks.value!!)
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun onClick(position: Int) {
        //책을 등록하는 bottom sheet 을 띄운다.
        val bottomSheet: AddBookBottomSheetFragment = AddBookBottomSheetFragment {
            when (it) {
                //읽는 중, 완독이면 -> 기록하기 화면으로 이동
                "NOW", "AFTER" -> (parentFragment as WriteFragment).moveToRecord()
                //읽고 싶은이면 -> 읽고 싶은 화면으로 이동.
                "BEFORE" -> {
                    binding.selectBookTV.visibility = View.VISIBLE
                    binding.readTypeRG.visibility = View.VISIBLE
                    binding.wantToReadRB.isChecked = true
                }
            }
        }
        bottomSheet.show((activity as MainActivity).supportFragmentManager, bottomSheet.tag)

        //클릭된 책 제목을 SelectBookViewModel의 selectedBookTitle 변수에 업데이트
        bookViewModel.updateSelectedBook(position)
    }

}