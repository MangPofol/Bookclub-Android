package com.mangpo.bookclub.view.write

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.mangpo.bookclub.databinding.FragmentSelectBookBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.view.adapter.BookAdapter
import com.mangpo.bookclub.view.adapter.OnItemClick
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.BookViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SelectFragment : Fragment(), android.text.TextWatcher, OnItemClick {
    private lateinit var binding: FragmentSelectBookBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var callback: OnBackPressedCallback

    private val bookViewModel: BookViewModel by sharedViewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        //뒤로가기 콜백: 기록하기 화면으로 전환.
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStackImmediate()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bookAdapter = BookAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectBookBinding.inflate(inflater, container, false)

        //뒤로가기 아이콘 누르면 -> 기록하기 화면
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        //책 검색 EditText에 TextChanged 리스너 등록
        binding.searchBookET.addTextChangedListener(this)

        //Recycler View 어댑터 설정
        bookAdapter.setBooks(bookViewModel.getBookList("NOW"))
        binding.bookListRV.adapter = bookAdapter
        binding.bookListRV.layoutManager = GridLayoutManager(this.context, 3)

        //읽는중, 완독, 읽고 싶은 체크박스를 클릭하면 book recycler view UI 업데이트
        binding.readTypeRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.readingRB.id -> bookAdapter.setBooks(bookViewModel.getBookList("NOW"))
                binding.readCompleteRB.id -> bookAdapter.setBooks(bookViewModel.getBookList("AFTER"))
                binding.wantToReadRB.id -> bookAdapter.setBooks(bookViewModel.getBookList("BEFORE"))
            }
        }

        observe()

        return binding.root
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("SelectFragment", "onDetach")

        callback.remove()
    }

    override fun onClick(position: Int) {
        val selectedBook: BookModel    //선택한 책

        //책을 등록하는 bottom sheet 을 띄운다.
        if (binding.readTypeRG.visibility == View.GONE) {    //새로운 책을 클릭한 상황이면
            selectedBook = BookModel(
                name = bookViewModel.searchedBooks.value!![position].title,
                isbn = bookViewModel.searchedBooks.value!![position].isbn,
                imgPath = bookViewModel.searchedBooks.value!![position].thumbnail
            )

            val bottomSheet: AddBookBottomSheetFragment = AddBookBottomSheetFragment {
                selectedBook.category = it

                when (it) {
                    //읽는 중, 완독이면 -> 기록하기 화면으로 이동
                    "NOW", "AFTER" -> {
                        bookViewModel.setSelectedBook(selectedBook)
                        parentFragmentManager.popBackStack()
                    }
                    //읽고 싶은이면 -> 읽고 싶은 화면으로 이동.
                    "BEFORE" -> {
                        CoroutineScope(Dispatchers.Main).launch {
                            bookViewModel.createBook(selectedBook)
                        }
                    }
                }
            }

            bottomSheet.show((activity as MainActivity).supportFragmentManager, bottomSheet.tag)
        } else {    //기존 책을 클릭한 상황이면
            selectedBook = when (binding.readTypeRG.checkedRadioButtonId) {
                binding.readingRB.id -> bookViewModel.nowBooks.value!![position]
                binding.readCompleteRB.id -> bookViewModel.afterBooks.value!![position]
                else -> bookViewModel.beforeBooks.value!![position]
            }
            bookViewModel.setSelectedBook(selectedBook)
            parentFragmentManager.popBackStack()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (count == 0) { //검색어가 비어있으면
            //내 책장에서 선택하기, 읽기 유형 선택 화면 VISIBLE
            binding.selectBookTV.visibility = View.VISIBLE
            binding.readTypeRG.visibility = View.VISIBLE
            binding.searchResultTv.visibility = View.GONE
            binding.searchResultLine.visibility = View.GONE

            when (binding.readTypeRG.checkedRadioButtonId) {    //선택돼 있는 읽는 유형에 따라 책 목록 보이도록 설정
                binding.readingRB.id -> bookAdapter.setBooks(bookViewModel.nowBooks.value)
                binding.readCompleteRB.id -> bookAdapter.setBooks(bookViewModel.afterBooks.value)
                binding.wantToReadRB.id -> bookAdapter.setBooks(bookViewModel.beforeBooks.value)
            }
        } else {    //검색어가 입력돼 있으면
            //내 책장에서 선택하기, 읽기 유형 선택 화면 INVISIBLE
            binding.selectBookTV.visibility = View.GONE
            binding.readTypeRG.visibility = View.GONE
            binding.searchResultTv.visibility = View.VISIBLE
            binding.searchResultLine.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.IO).launch { //입력된 검색어에 따라 책 목록 보이도록
                bookViewModel.getSearchedBooks(s.toString())
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    private fun observe() {
        //책 검색 시 book recycler view UI 가 업데이트됨.
        bookViewModel.searchedBooks.observe(viewLifecycleOwner, Observer {
            if (binding.searchBookET.text.isNotBlank())
                bookAdapter.setKakaoBooks(it)
        })

        bookViewModel.beforeBooks.observe(viewLifecycleOwner, Observer {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                (activity as MainActivity).moveBottomPager(1)
                bookViewModel.setReadType("BEFORE")
            }
        })
    }

}