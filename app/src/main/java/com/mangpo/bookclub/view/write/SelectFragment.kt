package com.mangpo.bookclub.view.write

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.mangpo.bookclub.databinding.FragmentSelectBookBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.util.HorizontalItemDecorator
import com.mangpo.bookclub.util.VerticalItemDecorator
import com.mangpo.bookclub.view.adapter.BookAdapter
import com.mangpo.bookclub.view.adapter.OnItemClick
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.BookViewModel
import kotlinx.coroutines.*

class SelectFragment : Fragment(), android.text.TextWatcher, OnItemClick {
    private lateinit var binding: FragmentSelectBookBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var callback: OnBackPressedCallback

    private var isNewBook: Boolean = false

    private val bookViewModel: BookViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("SelectFragment", "onAttach")

        //뒤로가기 콜백: 기록하기 화면으로 전환.
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("SelectBook", "onCreate")
        bookAdapter = BookAdapter(this)
        bookViewModel.updateSelectedBookReadType(0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectBookBinding.inflate(inflater, container, false)
        Log.e("SelectBook", "onCreateView")

        //뒤로가기 아이콘 누르면 -> 기록하기 화면
        binding.toolbar.setNavigationOnClickListener {
//            (requireParentFragment() as WriteFragment).moveToRecord()
            parentFragmentManager.popBackStack()
        }

        //책 검색 EditText에 TextChanged 리스너 등록
        binding.searchBookET.addTextChangedListener(this)

        //읽는중, 완독, 읽고 싶은 체크박스를 클릭하면 BookViewModel의 readType이 변경된다.
        binding.readTypeRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.readingRB.id -> bookViewModel.updateSelectedBookReadType(0)
                binding.readCompleteRB.id -> bookViewModel.updateSelectedBookReadType(1)
                binding.wantToReadRB.id -> bookViewModel.updateSelectedBookReadType(2)
            }
        }

        //Recycler View 어댑터 설정
        binding.bookListRV.adapter = bookAdapter
        binding.bookListRV.layoutManager = GridLayoutManager(this.context, 3)
        binding.bookListRV.addItemDecoration(VerticalItemDecorator(50))
        binding.bookListRV.addItemDecoration(HorizontalItemDecorator(20))

        //BookViewModel 의 readType 이 변경되면 recycler view 의 책 목록이 업데이트 된다.
        bookViewModel.selectedBookReadType.observe(viewLifecycleOwner, Observer {
            Log.e("selectedBookReadType observe", it.toString())

            (requireActivity() as MainActivity).hideKeyBord(this.requireView()) //키보드가 위로 올라와 있다면 키보드 내리기

            when (it) {
                0 -> {
                    bookAdapter.setBooks(bookViewModel.nowBooks.value!!)
                }
                1 -> bookAdapter.setBooks(bookViewModel.afterBooks.value!!)
                2 ->
                    bookAdapter.setBooks(bookViewModel.beforeBooks.value!!)
            }

        })

        //검색어에 따라 searchedBooks가 업데이트 되면 recycler view도 변경.
        bookViewModel.searchedBooks.observe(viewLifecycleOwner, Observer {
            if (binding.searchBookET.text.toString()!="")   //책 검색창이 비어 있으면 읽고 싶은 책, 아니면 검색 결과 목록으로
                bookAdapter.setKakaoBooks(it)
        })

        //읽고 싶은 책 목록이 업데이트 되면(아마 읽고 싶은에 책이 추가됐을 때만 그럴 것 같음) recycler view 의 책 목록을 업데이트 한다.
        /*bookViewModel.beforeBooks.observe(viewLifecycleOwner, Observer {
            Log.e("beforeBooks observe", it.toString())
            bookAdapter.setBooks(it)
        })*/

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        Log.e("SelectBook", "onPause")
//        (requireParentFragment() as WriteFragment).moveToRecord()
    }

    override fun onClick(position: Int) {
        //책을 등록하는 bottom sheet 을 띄운다.
        if (isNewBook) {    //새로운 책을 클릭한 상황이면
            //클릭된 책 제목을 SelectBookViewModel의 selectedBookTitle 변수에 업데이트
            val selectedBook = bookViewModel.searchedBooks.value!![position]
            bookViewModel.updateSelectedBook(
                BookModel(
                name = selectedBook.title,
                isbn = selectedBook.isbn
            )
            )
            val bottomSheet: AddBookBottomSheetFragment = AddBookBottomSheetFragment {
                when (it) {
                    //읽는 중, 완독이면 -> 기록하기 화면으로 이동
                    "NOW", "AFTER" -> {
                        parentFragmentManager.popBackStack()
//                        (requireParentFragment() as WriteFragment).moveToRecord()
                    }
                    //읽고 싶은이면 -> 읽고 싶은 화면으로 이동.
                    "BEFORE" -> {
                        Log.e("Select", bookViewModel.selectedBook.value.toString())

                        CoroutineScope(Dispatchers.Main).launch {
                            val code = CoroutineScope(Dispatchers.IO).async {
                                bookViewModel.createBeforeBook(bookViewModel.selectedBook.value!!)
                            }

                            when (code.await()) {
                                201 -> {
                                    Toast.makeText(context, "책 추가 성공!", Toast.LENGTH_SHORT).show()
                                    (requireActivity() as MainActivity).goToBeforeLibrary()
                                }
                                400 -> {
                                    Toast.makeText(context, "이미 등록된 책입니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        /*bookViewModel.updateBeforeBook(selectedBookViewModel.createdBook.value!!)
                        binding.selectBookTV.visibility = View.VISIBLE
                        binding.readTypeRG.visibility = View.VISIBLE
                        binding.wantToReadRB.isChecked = true*/
                    }
                }
            }
            bottomSheet.show((activity as MainActivity).supportFragmentManager, bottomSheet.tag)
        } else {    //기존 책을 클릭한 상황이면
            when (bookViewModel.selectedBookReadType.value) {
                0 -> bookViewModel.updateSelectedBook(bookViewModel.nowBooks.value!![position])
                1 -> bookViewModel.updateSelectedBook(bookViewModel.afterBooks.value!![position])
                2 -> bookViewModel.updateSelectedBook(bookViewModel.beforeBooks.value!![position])
            }
            parentFragmentManager.popBackStack()
//            (requireParentFragment() as WriteFragment).moveToRecord()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (count == 0) { //검색어가 비어있으면
            isNewBook = false    //기존 책을 보고 있는 중이라는 flag 로 변경.

            //내 책장에서 선택하기, 읽기 유형 선택 화면 VISIBLE
            binding.selectBookTV.visibility = View.VISIBLE
            binding.readTypeRG.visibility = View.VISIBLE

            when (bookViewModel.selectedBookReadType.value) {    //선택돼 있는 읽는 유형에 따라 책 목록 보이도록 설정
                0 -> bookAdapter.setBooks(bookViewModel.nowBooks.value!!)
                1 -> bookAdapter.setBooks(bookViewModel.afterBooks.value!!)
                2 -> bookAdapter.setBooks(bookViewModel.beforeBooks.value!!)
            }
        } else {    //검색어가 입력돼 있으면
            isNewBook = true    //새로운 책을 검색중이라는 flag 로 변경.

            //내 책장에서 선택하기, 읽기 유형 선택 화면 INVISIBLE
            binding.selectBookTV.visibility = View.INVISIBLE
            binding.readTypeRG.visibility = View.INVISIBLE

            CoroutineScope(Dispatchers.IO).launch { //입력된 검색어에 따라 책 목록 보이도록
                bookViewModel.updateSearchBookTitle(s.toString())
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun onDetach() {
        super.onDetach()
        Log.e("Select", "onDetach")
        callback.remove()
    }
}