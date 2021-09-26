package com.mangpo.bookclub.view.write

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentRecordBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.view.MainActivity
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class RecordFragment : Fragment() {
    private lateinit var binding: FragmentRecordBinding
    private lateinit var callback: OnBackPressedCallback

    private val bookViewModel: BookViewModel by activityViewModels<BookViewModel>()
    private val postViewModel: PostViewModel by activityViewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("Record", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordBinding.inflate(inflater, container, false)
        Log.e("Record", "onCreateView")

        //뒤로가기 콜백
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //책이 선택돼 있는 상태면
                if (!binding.selectBookBtn.text.equals(getString(R.string.book_select)))
                    //책 제목 지우고 기록할 책을 선택해주세요 로 바꾸기
                    binding.selectBookBtn.text = getString(R.string.book_select)
                else    //책이 선택돼 있는 상태가 아니면
                    requireActivity().finish()  //앱 종료
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val newPost: PostModel = PostModel()

        //selectedBook observer
        bookViewModel.selectedBook.observe(viewLifecycleOwner, Observer {
            if (it.name == "") { //빈값이면 책 선택 버튼에 "기록할 책을 선택하세요"
                binding.selectBookBtn.text = getString(R.string.book_select)
            } else {    //값이 존재하면 책 선택 버튼에 추가된 책의 이름
                binding.selectBookBtn.text = it.name
            }
        })

        binding.selectBookBtn.setOnClickListener {  //책 선택 버튼을 누르면 SelectBookFragment로 이동
            (requireParentFragment() as WriteFragment).moveToSelectBook()
        }

        //메모, 토픽 선택하면 newPost type 변경
        binding.memoTopicRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.memoRB.id -> newPost.type = "MEMO"
                binding.memoTopicRG.id -> newPost.type = "TOPIC"
            }
        }

        binding.postTV.setOnClickListener { //올리기 버튼을 클릭 리스너
            //모든 필수 항목을 다 입력했는지 유효성 검사
            when {
                binding.selectBookBtn.text.equals(getString(R.string.book_select)) -> Toast.makeText(
                    context,
                    "책을 선택해주세요",
                    Toast.LENGTH_SHORT
                ).show()
                binding.memoTopicRG.checkedRadioButtonId == -1 -> Toast.makeText(
                    context,
                    "메모/토픽 중 선택해주세요",
                    Toast.LENGTH_SHORT
                ).show()
                binding.postTitleET.text.isEmpty() -> Toast.makeText(
                    context,
                    "제목을 입력해주세요",
                    Toast.LENGTH_SHORT
                ).show()
                binding.contentET.text.isEmpty() -> Toast.makeText(
                    context,
                    "내용을 입력해주세요",
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    
                    /*val selectedBook: BookModel = bookViewModel.selectedBook.value!!
                    when (bookViewModel.selectedBookReadType.value) {
                        0 -> selectedBook.category = "NOW"
                        1 -> selectedBook.category = "AFTER"
                        2 -> selectedBook.category = "BEFORE"
                    }

                    if (selectedBook.id==null) {  //id가 null 이란 건 아직 등록되지 않은 책이란 뜻(첫번째 기록)
                        CoroutineScope(Dispatchers.Main).launch {
                            val code = bookViewModel.createBook(selectedBook)

                            if (code==200) {
                                newPost.bookId = bookViewModel.createdBook.value!!.id
                            }
                        }
                    } else {    //이미 등록돼 있던 책(기록 데이터가 존재하는 책)
                        newPost.bookId = selectedBook.id
                    }

                    //new post 데이터 만들기
                    newPost.scope = "PUBLIC"
                    newPost.isIncomplete = false
                    newPost.title = binding.postTitleET.text.toString()
                    newPost.content = binding.contentET.text.toString()

                    CoroutineScope(Dispatchers.Main).launch {

                    }*/

                    /*Log.e("Record", selectedBookViewModel.selectedBook.value.toString())
                    Log.e("Record", "책 제목 -> ${selectedBookViewModel.selectedBook.value!!.name}")
                    Log.e("Record", "메모/토픽 -> ${binding.memoTopicRG.checkedRadioButtonId}")
                    Log.e("Record", "기록하기 제목 -> ${binding.postTitleET.text}")
                    Log.e("Record", "내용 -> ${binding.contentET.text}")*/
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("Record", "onViewCreated")

        (activity as MainActivity).setDrawer(binding.toolbar)   //navigation drawer 등록
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_more_vert_36_black)  //navigation icon 설정
    }

    override fun onPause() {
        super.onPause()
        bookViewModel.clearSelectedBook()
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("Record", "onDetach")

        callback.remove()
    }
}