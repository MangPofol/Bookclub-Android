package com.example.bookclub.view.write

import android.os.Bundle
import android.text.Editable
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
import com.example.bookclub.viewmodel.SelectedBookViewModel
import kotlinx.coroutines.*

class SelectBookFragment : Fragment(), android.text.TextWatcher, OnBookItemClick {
    private lateinit var binding: FragmentSelectBookBinding
    private lateinit var bookAdapter: BookAdapter

    private val bookViewModel: BookViewModel by activityViewModels()
    private val selectedBookViewModel: SelectedBookViewModel by activityViewModels<SelectedBookViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("SelectBook", "onCreate")
        bookAdapter = BookAdapter(this)
        selectedBookViewModel.updateSelectedBookReadType(0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectBookBinding.inflate(inflater, container, false)
        Log.e("SelectBook", "onCreateView")

        //뒤로가기 아이콘 누르면 -> 기록하기 화면
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        //책 검색 EditText에 TextChanged 리스너 등록
        binding.searchBookET.addTextChangedListener(this)

        //읽는중, 완독, 읽고 싶은 체크박스를 클릭하면 BookViewModel의 readType이 변경된다.
        binding.readTypeRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.readingRB.id -> selectedBookViewModel.updateSelectedBookReadType(0)
                binding.readCompleteRB.id -> selectedBookViewModel.updateSelectedBookReadType(1)
                binding.wantToReadRB.id -> selectedBookViewModel.updateSelectedBookReadType(2)
            }
        }

        //Recycler View 어댑터 설정
        binding.bookListRV.adapter = bookAdapter
        binding.bookListRV.layoutManager = GridLayoutManager(this.context, 3)
        binding.bookListRV.addItemDecoration(VerticalItemDecorator(50))
        binding.bookListRV.addItemDecoration(HorizontalItemDecorator(20))

        //BookViewModel 의 readType 이 변경되면 recycler view 의 책 목록이 업데이트 된다.
        selectedBookViewModel.selectedBookReadType.observe(viewLifecycleOwner, Observer {
            Log.e("selectedBookReadType observe", it.toString())

            when (it) {
                0 -> bookAdapter.setBooks(bookViewModel.nowBooks.value!!)
                1 -> bookAdapter.setBooks(bookViewModel.afterBooks.value!!)
                2 -> bookAdapter.setBooks(bookViewModel.beforeBooks.value!!)
            }

        })

        //검색어에 따라 searchedBooks가 업데이트 되면 recycler view도 변경.
        bookViewModel.searchedBooks.observe(viewLifecycleOwner, Observer {
            bookAdapter.setKakaoBooks(it)
        })

        //읽고 싶은 책 목록이 업데이트 되면(아마 읽고 싶은에 책이 추가됐을 때만 그럴 것 같음) recycler view 의 책 목록을 업데이트 한다.
        /*bookViewModel.beforeBooks.observe(viewLifecycleOwner, Observer {
            if (bookViewModel.readType.value == 2)
                bookAdapter.setBooks(it)
        })*/

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        Log.e("SelectBook", "onPause")
        (requireParentFragment() as WriteFragment).moveToRecord()
    }

    override fun onClick(position: Int) {
        //책을 등록하는 bottom sheet 을 띄운다.
        val bottomSheet: AddBookBottomSheetFragment = AddBookBottomSheetFragment {
            when (it) {
                //읽는 중, 완독이면 -> 기록하기 화면으로 이동
                "NOW", "AFTER" -> {
                    (requireParentFragment() as WriteFragment).moveToRecord()
                }
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

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (count==0) { //겁색어가 비어있으면
            //내 책장에서 선택하기, 읽기 유형 선택 화면 VISIBLE
            binding.selectBookTV.visibility = View.VISIBLE
            binding.readTypeRG.visibility = View.VISIBLE

            /*when(bookViewModel.readType.value) {    //선택돼 있는 읽는 유형에 따라 책 목록 보이도록 설정
                0 -> bookAdapter.setBooks(bookViewModel.nowBooks.value!!)
                1 -> bookAdapter.setBooks(bookViewModel.afterBooks.value!!)
                2 -> bookAdapter.setBooks(bookViewModel.beforeBooks.value!!)
            }*/
        } else {    //검색어가 입력돼 있으면
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

}