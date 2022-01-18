package com.mangpo.bookclub.view.library

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        //읽는중, 읽고 싶은 -> 완독 OR 완독 -> 읽는중 으로 변경
        binding.categoryBtn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if (book.category == "AFTER") { //완독 -> 읽는중으로 변경
                    val result = CoroutineScope(Dispatchers.IO).async {
                        bookVm.updateBook(book.id!!, "NOW")
                    }

                    if (result.await()) {
                        book.category = "NOW"

                        binding.categoryBtn.background =
                            requireContext().getDrawable(R.drawable.bg_book_desc_category_after_btn)
                        binding.categoryBtn.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_read_after,
                            0
                        )

                        Toast.makeText(
                            this@BookDetailFragment.context,
                            "책 완독을 취소했습니다!",
                            Toast.LENGTH_SHORT
                        ).show()

                        initBookList()
                    } else {
                        Toast.makeText(requireContext(), "오류 발생. 다시 시도해 주세요.", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {    //읽는중 -> 완독으로 변경
                    val result = CoroutineScope(Dispatchers.IO).async {
                        bookVm.updateBook(book.id!!, "AFTER")
                    }

                    if (result.await()) {
                        book.category = "AFTER"

                        binding.categoryBtn.background =
                            requireContext().getDrawable(R.drawable.bg_book_desc_category_not_after_btn)
                        binding.categoryBtn.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_read_not_after,
                            0
                        )

                        Toast.makeText(
                            this@BookDetailFragment.context,
                            "첵을 완독하셧습니다.",
                            Toast.LENGTH_SHORT
                        ).show()

                        initBookList()
                    } else {
                        Toast.makeText(requireContext(), "오류 발생. 다시 시도해 주세요.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        }

        //기록 쓰러가기 버튼 클릭 리스너
        binding.goRecordBtn.setOnClickListener {
            bookVm.setBook(book)
            (requireActivity() as MainActivity).moveToRecord(false)
        }

        //삭제 텍스트뷰 클릭 리스너
        binding.deleteBtn.setOnClickListener {
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

        if (book.category == "AFTER") {
            binding.categoryBtn.background =
                requireContext().getDrawable(R.drawable.bg_book_desc_category_not_after_btn)
            binding.categoryBtn.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_read_not_after,
                0
            )
        } else {
            binding.categoryBtn.background =
                requireContext().getDrawable(R.drawable.bg_book_desc_category_after_btn)
            binding.categoryBtn.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_read_after,
                0
            )
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