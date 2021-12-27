package com.mangpo.bookclub.view.write

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.mangpo.bookclub.databinding.FragmentPostDetailBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.PostDetailModel
import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.util.BackStackManager
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.ArrayList

class PostDetailFragment : Fragment() {

    private val postVm: PostViewModel by sharedViewModel()
    private val bookVm: BookViewModel by sharedViewModel()

    private var bookId: Long? = null
    private var bookName: String? = null
    private var isInit: Boolean = false

    private lateinit var binding: FragmentPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PostDetailFragment", "onCreate")

        bookId = arguments?.getLong("bookId")
        bookName = arguments?.getString("bookName")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("PostDetailFragment", "onCreateView")

        binding = FragmentPostDetailBinding.inflate(inflater, container, false)

        setUI() //화면 디자인 함수 호출

        binding.backIvView.setOnClickListener {
            (requireActivity() as MainActivity).changeFragment(BackStackManager.popFragment()!!)
        }

        //기록 수정 화면으로 이동
        binding.updateTv.setOnClickListener {
            setBook()
            setPost()

            val fragment = RecordFragment(true)
            (requireActivity() as MainActivity).changeFragment(fragment)
            BackStackManager.pushFragment(0, fragment)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("PostDetailFragment", "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("PostDetailFragment", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("PostDetailFragment", "onDestroyView")

        if (isInit) {
            postVm.setPostDetail(null)
            arguments?.remove("bookId")
            arguments?.remove("bookName")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("PostDetailFragment", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("PostDetailFragment", "onDetach")
    }

    //화면 디자인 함수
    private fun setUI() {
        val post = postVm.getPostDetail()!!

        binding.bookNameTv.text = arguments?.getString("bookName")

        if (post.scope == "PRIVATE")
            binding.updateTv.visibility = View.VISIBLE
        else
            binding.updateTv.visibility = View.INVISIBLE

        binding.recordTitleTv.text = post.title
        binding.recordContentTv.text = post.content
        setImg(post.postImgLocations)   //이미지 개수에 따라 ImageView 구성을 바꾸는 함수 호출

        binding.locationTv.text = post.location
        binding.clockTv.text = post.readTime
        /*binding.likeCntTv.text = post.likedList.size.toString()
        binding.commentCntTv.text = post.commentsDto.size.toString()*/
    }

    //이미지 개수에 따라 ImageView 구성을 바꾸는 함수
    private fun setImg(imgs: List<String>) {

        when {
            imgs.isEmpty() -> {
                setVisibilityOneIV(View.GONE)
                setVisibilityTwoIV(View.GONE)
                setVisibilityThreeIV(View.GONE)
                setVisibilityFourIV(View.GONE)
            }
            imgs.size == 1 -> {
                setVisibilityOneIV(View.VISIBLE)
                setVisibilityTwoIV(View.GONE)
                setVisibilityThreeIV(View.GONE)
                setVisibilityFourIV(View.GONE)

                Glide.with(requireContext().applicationContext).load(imgs[0]).into(binding.oneIv)
            }
            imgs.size == 2 -> {
                setVisibilityOneIV(View.GONE)
                setVisibilityTwoIV(View.VISIBLE)
                setVisibilityThreeIV(View.GONE)
                setVisibilityFourIV(View.GONE)

                Glide.with(requireContext().applicationContext).load(imgs[0]).into(binding.twoIv1)
                Glide.with(requireContext().applicationContext).load(imgs[1]).into(binding.twoIv2)
            }
            imgs.size == 3 -> {
                setVisibilityOneIV(View.GONE)
                setVisibilityTwoIV(View.GONE)
                setVisibilityThreeIV(View.VISIBLE)
                setVisibilityFourIV(View.GONE)

                Glide.with(requireContext().applicationContext).load(imgs[0]).into(binding.threeIv1)
                Glide.with(requireContext().applicationContext).load(imgs[1]).into(binding.threeIv2)
                Glide.with(requireContext().applicationContext).load(imgs[2]).into(binding.threeIv3)
            }
            else -> {
                setVisibilityOneIV(View.GONE)
                setVisibilityTwoIV(View.GONE)
                setVisibilityThreeIV(View.GONE)
                setVisibilityFourIV(View.VISIBLE)

                Glide.with(requireContext().applicationContext).load(imgs[0]).into(binding.fourIv1)
                Glide.with(requireContext().applicationContext).load(imgs[1]).into(binding.fourIv2)
                Glide.with(requireContext().applicationContext).load(imgs[2]).into(binding.fourIv3)
                Glide.with(requireContext().applicationContext).load(imgs[3]).into(binding.fourIv4)
            }
        }

    }

    private fun setVisibilityOneIV(visibility: Int) {
        binding.oneIv.visibility = visibility
        binding.oneIv.setOnClickListener {
            goPhotoViewActivity(0)
        }
    }

    private fun setVisibilityTwoIV(visibility: Int) {
        binding.twoIv1.visibility = visibility
        binding.twoIv2.visibility = visibility

        binding.twoIv1.setOnClickListener {
            goPhotoViewActivity(0)
        }
        binding.twoIv2.setOnClickListener {
            goPhotoViewActivity(1)
        }
    }

    private fun setVisibilityThreeIV(visibility: Int) {
        binding.threeIv1.visibility = visibility
        binding.threeIv2.visibility = visibility
        binding.threeIv3.visibility = visibility

        binding.threeIv1.setOnClickListener {
            goPhotoViewActivity(0)
        }
        binding.threeIv2.setOnClickListener {
            goPhotoViewActivity(1)
        }
        binding.threeIv3.setOnClickListener {
            goPhotoViewActivity(2)
        }
    }

    private fun setVisibilityFourIV(visibility: Int) {
        binding.fourIv1.visibility = visibility
        binding.fourIv2.visibility = visibility
        binding.fourIv3.visibility = visibility
        binding.fourIv4.visibility = visibility

        binding.fourIv1.setOnClickListener {
            goPhotoViewActivity(0)
        }
        binding.fourIv2.setOnClickListener {
            goPhotoViewActivity(1)
        }
        binding.fourIv3.setOnClickListener {
            goPhotoViewActivity(2)
        }
        binding.fourIv4.setOnClickListener {
            goPhotoViewActivity(3)
        }
    }

    private fun setBook() {
        val book: BookModel = BookModel(id = bookId!!, name = bookName!!)
        bookVm.setBook(book)
    }

    private fun setPost() {
        val postDetail: PostDetailModel = postVm.getPostDetail()!!
        val post: PostModel = PostModel(
            bookId,
            postDetail.scope,
            false,
            postDetail.location,
            postDetail.readTime,
            postDetail.hyperlinkTitle,
            postDetail.hyperlink,
            postDetail.title,
            postDetail.content,
            postDetail.postImgLocations
        )

        postVm.setPost(post)
    }

    private fun goPhotoViewActivity(position: Int) {
        isInit = false

        val intent: Intent = Intent(requireContext(), PhotoViewActivity::class.java)
        intent.putStringArrayListExtra(
            "imgs",
            postVm.getPostDetail()!!.postImgLocations as ArrayList<String>
        )
        intent.putExtra("currentItem", position)
        startActivity(intent)

        (requireActivity() as MainActivity).setLatestFragment("PostDetail")
    }

}