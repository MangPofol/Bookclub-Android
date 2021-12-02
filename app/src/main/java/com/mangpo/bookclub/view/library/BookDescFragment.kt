package com.mangpo.bookclub.view.library

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentBookDescBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.PostDetailModel
import com.mangpo.bookclub.view.adapter.PostAdapter
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BookDescFragment : Fragment() {

    private val postVm: PostViewModel by sharedViewModel()
    private val bookVm: BookViewModel by sharedViewModel()

    private lateinit var binding: FragmentBookDescBinding
    private lateinit var book: BookModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        book = Gson().fromJson<BookModel>(arguments?.getString("book"), BookModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBookDescBinding.inflate(inflater, container, false)

        initUI(book)
        initAdapter()

        binding.backIv.setOnClickListener {
            requireParentFragment().childFragmentManager.popBackStackImmediate()
        }

        binding.readCompleteView.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                if (book.category == "AFTER") {
                    val result = CoroutineScope(Dispatchers.IO).async {
                        bookVm.updateBook(book.id!!, "NOW")
                    }

                    if (result.await()) {
                        binding.readCompleteView.background =
                            requireContext().getDrawable(R.drawable.book_desc_read_not_complete_view)
                        Toast.makeText(
                            this@BookDescFragment.context,
                            "책 완독을 취소했습니다!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        initBookList()
                    } else {
                        Toast.makeText(requireContext(), "오류 발생. 다시 시도해 주세요.", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    val result = CoroutineScope(Dispatchers.IO).async {
                        bookVm.updateBook(book.id!!, "AFTER")
                    }

                    if (result.await()) {
                        binding.readCompleteView.background =
                            requireContext().getDrawable(R.drawable.book_desc_read_complete_view)
                        Toast.makeText(
                            this@BookDescFragment.context,
                            "첵을 완독하셧습니다.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        initBookList()
                    } else {
                        Toast.makeText(requireContext(), "오류 발생. 다시 시도해 주세요.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        }

        return binding.root
    }

    private fun initBookList() {
        val email: String = (requireActivity() as MainActivity).getEmail()
        CoroutineScope(Dispatchers.Main).launch {
            bookVm.requestBookList(email, "NOW")
            bookVm.requestBookList(email, "AFTER")
        }
    }

    private fun initUI(book: BookModel) {
        binding.bookNameTv.text = book.name
        binding.bookIv.clipToOutline = true
        Glide.with(this).load(book.imgPath).into(binding.bookIv)

        if (book.category == "AFTER") {
            binding.readCompleteView.background =
                requireContext().getDrawable(R.drawable.book_desc_read_complete_view)
        } else {
            binding.readCompleteView.background =
                requireContext().getDrawable(R.drawable.book_desc_read_not_complete_view)
        }
    }

    private fun initAdapter() {
        val postAdapter: PostAdapter = PostAdapter()
        binding.memoListRv.adapter = postAdapter

        postAdapter.setMyItemClickListener(object : PostAdapter.MyItemClickListener {
            override fun goPostDetail(post: PostDetailModel) {
                postVm.setPostDetail(post)
                (requireActivity() as MainActivity).goBookDesc(book.id!!, book.name)
            }

        })

        CoroutineScope(Dispatchers.Main).launch {
            val posts = CoroutineScope(Dispatchers.IO).async {
                postVm.getPosts(book.id!!, null)
            }

            postAdapter.setData(posts.await()!!.sortedByDescending { it.createdDate })
        }
    }

}