package com.mangpo.bookclub.view.write

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
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
        setImg(post.postImgLocations)


        binding.locationTv.text = post.location
        binding.clockTv.text = post.readTime
        binding.likeCntTv.text = post.likedList.size.toString()
        binding.commentCntTv.text = post.commentsDto.size.toString()
    }

    private fun setImg(imgs: List<String>) {

        when {
            imgs.isEmpty() -> {
                setVisibilityOneIV(View.GONE)
                setVisibilityTwoIV(View.GONE)
                setVisibilityThreeIV(View.GONE)
                setVisibilityFourIV(View.GONE)
            }
            imgs.size==1 -> {
                setVisibilityOneIV(View.VISIBLE)
                setVisibilityTwoIV(View.GONE)
                setVisibilityThreeIV(View.GONE)
                setVisibilityFourIV(View.GONE)

                Glide.with(requireContext()).load(imgs[0]).into(binding.oneIv)
            }
            imgs.size==2 -> {
                setVisibilityOneIV(View.GONE)
                setVisibilityTwoIV(View.VISIBLE)
                setVisibilityThreeIV(View.GONE)
                setVisibilityFourIV(View.GONE)

                Glide.with(requireContext()).load(imgs[0]).into(binding.twoIv1)
                Glide.with(requireContext()).load(imgs[1]).into(binding.twoIv2)
            }
            imgs.size==3 -> {
                setVisibilityOneIV(View.GONE)
                setVisibilityTwoIV(View.GONE)
                setVisibilityThreeIV(View.VISIBLE)
                setVisibilityFourIV(View.GONE)

                Glide.with(requireContext()).load(imgs[0]).into(binding.threeIv1)
                Glide.with(requireContext()).load(imgs[1]).into(binding.threeIv2)
                Glide.with(requireContext()).load(imgs[2]).into(binding.threeIv3)
            }
            else -> {
                setVisibilityOneIV(View.GONE)
                setVisibilityTwoIV(View.GONE)
                setVisibilityThreeIV(View.GONE)
                setVisibilityFourIV(View.VISIBLE)

                Glide.with(requireContext()).load(imgs[0]).into(binding.fourIv1)
                Glide.with(requireContext()).load(imgs[1]).into(binding.fourIv2)
                Glide.with(requireContext()).load(imgs[2]).into(binding.fourIv3)
                Glide.with(requireContext()).load(imgs[3]).into(binding.fourIv4)
            }
        }

    }

    private fun setVisibilityOneIV(visibility: Int) {
        binding.oneIv.visibility = visibility
    }

    private fun setVisibilityTwoIV(visibility: Int) {
        binding.twoIv1.visibility = visibility
        binding.twoIv2.visibility = visibility
    }

    private fun setVisibilityThreeIV(visibility: Int) {
        binding.threeIv1.visibility = visibility
        binding.threeIv2.visibility = visibility
        binding.threeIv3.visibility = visibility
    }

    private fun setVisibilityFourIV(visibility: Int) {
        binding.fourIv1.visibility = visibility
        binding.fourIv2.visibility = visibility
        binding.fourIv3.visibility = visibility
        binding.fourIv4.visibility = visibility
    }

}