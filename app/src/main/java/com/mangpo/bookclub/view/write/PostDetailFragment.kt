package com.mangpo.bookclub.view.write

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.mangpo.bookclub.databinding.FragmentPostDetailBinding
import com.mangpo.bookclub.model.PostDetailModel
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.collections.ArrayList

class PostDetailFragment : Fragment() {

    private val postVm: PostViewModel by sharedViewModel()
    private val bookVm: BookViewModel by sharedViewModel()

    private var isInit: Boolean = false

    private lateinit var binding: FragmentPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PostDetailFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("PostDetailFragment", "onCreateView")

        binding = FragmentPostDetailBinding.inflate(inflater, container, false)

        setUI() //화면 디자인 함수 호출

        //뒤로가기 클릭 리스너
        binding.backIvView.setOnClickListener {
            (requireActivity() as MainActivity).onBackPressed()
        }

        //수정하기 클릭 리스너
        binding.updateTv.setOnClickListener {
//            setBook()   //수정하고자 하는 책 데이터 저장
            //수정하고자 하는 기록 데이터 저장 & RecordFragment 화면으로 이동
            (requireActivity() as MainActivity).moveToRecord(true)
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
        //현재 post 를 BookDescFragment 또는 WritingSettingFragment 로부터 전달받는다.
        val post = Gson().fromJson(arguments?.getString("post"), PostDetailModel::class.java)

        binding.bookNameTv.text = post.book!!.name

        if (post.scope == "PRIVATE")
            binding.updateTv.visibility = View.VISIBLE
        else
            binding.updateTv.visibility = View.INVISIBLE

        binding.recordTitleTv.text = post.title
        binding.recordContentTv.text = post.content
        setImg(post.postImgLocations)   //이미지 개수에 따라 ImageView 구성을 바꾸는 함수 호출

        binding.locationTv.text = post.location
        binding.clockTv.text = post.readTime

        //베타 버전 출시 후 사용
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

    /*private fun setBook() {
        val book: BookModel = BookModel(id = bookId!!, name = bookName!!)
        bookVm.setBook(book)
    }*/

    /*private fun getPost(): PostModel {
        val postDetail: PostDetailModel = postVm.getPostDetail()!!

        return PostModel(
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

//        postVm.setPost(post)
    }*/

    private fun goPhotoViewActivity(position: Int) {
        isInit = false

        //수정 후 사진 하나일 때 형변환 에러 해결을 위해
        var postImgLocations =
            if (postVm.getPostDetail()!!.postImgLocations.javaClass.toString() == "class java.util.Collections\$SingletonList")
                arrayListOf(postVm.getPostDetail()!!.postImgLocations[0])
            else
                postVm.getPostDetail()!!.postImgLocations as ArrayList<String>

        val intent: Intent = Intent(requireContext(), PhotoViewActivity::class.java)
        intent.putStringArrayListExtra(
            "imgs",
            postImgLocations
        )
        intent.putExtra("currentItem", position)
        startActivity(intent)
    }

}