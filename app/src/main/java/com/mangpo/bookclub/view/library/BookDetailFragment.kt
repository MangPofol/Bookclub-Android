package com.mangpo.bookclub.view.library

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentBookDetailBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.PostDetailModel
import com.mangpo.bookclub.view.adapter.PostAdapter
import com.mangpo.bookclub.view.dialog.RemoveDialogFragment
import com.mangpo.bookclub.view.MainActivity
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BookDetailFragment : Fragment() {

    private val postVm: PostViewModel by sharedViewModel()
    private val bookVm: BookViewModel by sharedViewModel()

    private lateinit var binding: FragmentBookDetailBinding
    private lateinit var book: BookModel
    private lateinit var removeDialogFragment: RemoveDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("BookDescFragment", "onCreate")

        book = Gson().fromJson<BookModel>(arguments?.getString("book"), BookModel::class.java)

        removeDialogFragment = RemoveDialogFragment(getString(R.string.title_delete_book)) {
            deleteCallback(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("BookDescFragment", "onCreateView")
        binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        observe()

        initUI(book)    //book 데이터를 가지고 바인딩 할 수 있는 UI 업데이트 함수 호출
        initAdapter()   //메모 리사이클러뷰 UI 업데이트 함수 호출

        //뒤로가기 클릭 리스너
        binding.backIvView.setOnClickListener {
            (requireActivity() as MainActivity).onBackPressed()
        }

        //읽는중 클릭 리스너
        binding.readingView.setOnClickListener {
//            Toast.makeText(requireContext(), "읽는 중 클릭", Toast.LENGTH_SHORT).show()
            changeCategory("NOW")
        }

        //완독 클릭 리스너
        binding.afterView.setOnClickListener {
//            Toast.makeText(requireContext(), "완독 클릭", Toast.LENGTH_SHORT).show()
            changeCategory("AFTER")
        }

        //기록 쓰러가기 버튼 클릭 리스너
        binding.goRecordView.setOnClickListener {
            bookVm.setBook(book)
            (requireActivity() as MainActivity).moveToRecord(false)
        }

        //삭제 뷰 클릭 리스너
        binding.deleteBookView.setOnClickListener {
            removeDialogFragment.show(requireActivity().supportFragmentManager, null)
        }

        //삭제 텍스트뷰 클릭 리스너
        binding.deleteBookTv.setOnClickListener {
            removeDialogFragment.show(requireActivity().supportFragmentManager, null)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("BookDescFragment", "onResume")

        initUI(book)
    }

    private fun initBookList() {
        CoroutineScope(Dispatchers.Main).launch {
            bookVm.requestBookList("NOW")
            bookVm.requestBookList("AFTER")
        }
    }

    //book 데이터를 가지고 바인딩 할 수 있는 UI 업데이트 함수
    private fun initUI(book: BookModel) {
        binding.bookTv.text = book.name
        binding.bookIv.clipToOutline = true
        Glide.with(requireActivity().applicationContext).load(book.imgPath).into(binding.bookIv)
        setBookInfoUI(book.category)
    }

    private fun setBookInfoUI(category: String) {
        when (category) {
            "AFTER" -> {    //완독
                binding.categoryIv.setImageResource(R.drawable.ic_eye_grey)
                binding.categoryTv.isEnabled = true
                binding.readingView.isEnabled = true

                binding.readCheckIv.setImageResource(R.drawable.ic_check_circle_bg_blue)
                binding.readCheckTv.isEnabled = false
                binding.afterView.isEnabled = false
            }
            "NOW" -> {  //읽는 중
                binding.categoryIv.setImageResource(R.drawable.ic_eye_blue)
                binding.categoryTv.isEnabled = false
                binding.readingView.isEnabled = false

                binding.readCheckIv.setImageResource(R.drawable.ic_check_circle_bg_grey)
                binding.readCheckTv.isEnabled = true
                binding.afterView.isEnabled = true
            }
            else -> {   //읽고 싶은
                binding.categoryIv.setImageResource(R.drawable.ic_eye_grey)
                binding.categoryTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey12
                    )
                )
                binding.readingView.isEnabled = false

                binding.readCheckIv.setImageResource(R.drawable.ic_check_circle_bg_grey)
                binding.readCheckTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey12
                    )
                )
                binding.afterView.isEnabled = false
            }
        }
    }

    //메모 리사이클러뷰 UI 업데이트 함수
    private fun initAdapter() {
        val postAdapter: PostAdapter = PostAdapter()
        binding.memoListRv.adapter = postAdapter

        CoroutineScope(Dispatchers.Main).launch {
            val posts = CoroutineScope(Dispatchers.IO).async {
                postVm.getPosts(book.id!!, null)
            }

            postAdapter.setData(posts.await()!!.sortedByDescending { it.createdDate })
        }

        postAdapter.setMyItemClickListener(object : PostAdapter.MyItemClickListener {
            //기록데이터 클릭 리스너
            override fun goPostDetail(post: PostDetailModel) {
                post.book = book    //선택된 기록 데이터에 현재 책 데이터를 저장한다.
                post.bookId = book.id   //선택된 기록 데이터에 현재 책 데이터 Id 를 저장한다.
                postVm.setPostDetail(post)  //현재 기록 데이터를 저장하고
                (requireActivity() as MainActivity).moveToPostDetail(post)  //PostDetailFragment 로 이동한다.
            }
        })
    }

    private fun deleteCallback(isDelete: Boolean) {
        if (isDelete) { //삭제 요청 보내기
            deleteBook()
        }
    }

    private fun deleteBook() {
        CoroutineScope(Dispatchers.IO).launch {
            bookVm.deleteBook(book.id!!)
        }
    }

    private fun changeCategory(changedCategory: String) {
        CoroutineScope(Dispatchers.Main).launch {

            if (changedCategory == "NOW") { //완독 -> 읽는중으로 변경
                val result = CoroutineScope(Dispatchers.IO).async {
                    bookVm.updateBook(book.id!!, "NOW")
                }

                if (result.await()) {
                    book.category = "NOW"
                    setBookInfoUI(book.category)

                    Toast.makeText(
                        this@BookDetailFragment.context,
                        getString(R.string.msg_read_complete),
                        Toast.LENGTH_SHORT
                    ).show()

                    initBookList()
                } else
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.err_change),
                        Toast.LENGTH_SHORT
                    ).show()
            } else {    //읽는중 -> 완독으로 변경
                val result = CoroutineScope(Dispatchers.IO).async {
                    bookVm.updateBook(book.id!!, "AFTER")
                }

                if (result.await()) {
                    book.category = "AFTER"
                    setBookInfoUI(book.category)

                    Toast.makeText(
                        this@BookDetailFragment.context,
                        getString(R.string.msg_cancel_read_complete),
                        Toast.LENGTH_SHORT
                    ).show()

                    initBookList()
                } else
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.err_change),
                        Toast.LENGTH_SHORT
                    ).show()
            }

        }
    }

    private fun observe() {
        bookVm.deleteBookCode.observe(viewLifecycleOwner, Observer {
            Log.d("BookDescFragment", "deleteBookCode Observe!! - $it")

            if (it == 204) {
                CoroutineScope(Dispatchers.Main).launch {
                    bookVm.requestBookList(book.category)   //삭제한 책이 있던 카테고리의 책 목록 데이터 업데이트
                    bookVm.setBook(BookModel()) //현재 선택된 책을 표시하는 뷰모델의 book 데이터 초기화
                    (requireActivity() as MainActivity).onBackPressed() //현재 프래그먼트 나가기
                    bookVm.setDeleteBookCode(-1)    //책 삭제 코드 -1(초깃값)로 설정
                }
            }
        })
    }
}