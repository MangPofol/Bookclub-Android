package com.mangpo.bookclub.view.write

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mangpo.bookclub.databinding.FragmentPostDetailBinding
import com.mangpo.bookclub.viewmodel.PostViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PostDetailFragment : Fragment() {

    private val postVm: PostViewModel by sharedViewModel()

    private var bookId: Long? = null

    private lateinit var binding: FragmentPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PostDetailFragment", "onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        Log.d("PostDetailFragment", "onCreateView")

        bookId = arguments?.getLong("bookId")

        setUI()

        return binding.root
    }

    private fun setUI() {
        val post = postVm.getPostDetail()!!

        binding.bookNameTv.text = arguments?.getString("bookName")

        if (post.scope == "PRIVATE")
            binding.updateTv.visibility = View.VISIBLE
        else
            binding.updateTv.visibility = View.INVISIBLE

        binding.recordTitleTv.text = post.title
        binding.recordContentTv.text = post.content

        if (post.postImgLocations.isEmpty()) {
            binding.recordImgIv.visibility = View.GONE
        } else {
            binding.recordImgIv.visibility = View.VISIBLE
        }

        binding.locationTv.text = post.location
        binding.clockTv.text = post.readTime
        binding.likeCntTv.text = post.likedList.size.toString()
        binding.commentCntTv.text = post.commentsDto.size.toString()
    }

}