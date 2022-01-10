package com.mangpo.bookclub.view.write

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.mangpo.bookclub.databinding.FragmentPostDetailBinding
import com.mangpo.bookclub.model.PostDetailModel
import com.mangpo.bookclub.view.adapter.PostDetailImgAdapter
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.collections.ArrayList

class PostDetailFragment : Fragment() {

    private val postVm: PostViewModel by sharedViewModel()
    private val bookVm: BookViewModel by sharedViewModel()

    private lateinit var binding: FragmentPostDetailBinding
    private lateinit var post: PostDetailModel
    private lateinit var postDetailImgAdapter: PostDetailImgAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PostDetailFragment", "onCreate")

        //현재 post 를 BookDescFragment 또는 WritingSettingFragment 로부터 전달받는다.
        post = Gson().fromJson(arguments?.getString("post"), PostDetailModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("PostDetailFragment", "onCreateView")

        binding = FragmentPostDetailBinding.inflate(inflater, container, false)

        initAdapter()
        setUI() //화면 디자인 함수 호출

        //수정하기 클릭 리스너
        binding.updateTv.setOnClickListener {
            //PostDetail -> Record(수정하기) -> 뒤로가기 -> 뒤로가기(PostDetail) -> Record(수정하기)로 오면
            //post 의 book 데이터가 null 로 바뀌는 버그를 막기 위해 bookViewModel 의 book 데이터에 다시 한 번 저장
            bookVm.setBook(post.book!!)
            postVm.setPost(post)    //수정하고자 하는 post 데이터를 PostViewModel 에 저장 & RecordFragment 화면으로 이동
            Log.d(
                "PostDetailFragment",
                "binding.updateTv.setOnClickListener post: ${postVm.getPost()}"
            )
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
        if (post.postImgLocations.isEmpty()) {
            binding.contentImgVp.visibility = View.GONE
        } else {
            binding.contentImgVp.visibility = View.VISIBLE
            //뷰페이저 어댑터에 데이터 바인딩
            postDetailImgAdapter.setData(post.postImgLocations)
            //인디케이터 등록
            binding.contentImgIndicator.setViewPager(binding.contentImgVp)
        }

        binding.bookNameTv.text = post.book!!.name

        if (post.scope == "PRIVATE")
            binding.updateTv.visibility = View.VISIBLE
        else
            binding.updateTv.visibility = View.INVISIBLE

        binding.recordTitleTv.text = post.title
        binding.recordContentTv.text = post.content

        binding.locationTv.text = post.location
        binding.clockTv.text = post.readTime
        binding.linkTv.text = post.hyperlink
    }

    //뷰페이저 어댑터 초기화
    private fun initAdapter() {
        postDetailImgAdapter = PostDetailImgAdapter()
        binding.contentImgVp.adapter = postDetailImgAdapter

        postDetailImgAdapter.setMyItemClickListener(object :
            PostDetailImgAdapter.MyItemClickListener {
            override fun goPhotoView(position: Int) {
                goPhotoViewActivity(position)
            }

        })
    }

    //이미지 상세 화면(PhotoViewActivity)으로 이동
    private fun goPhotoViewActivity(position: Int) {
        //수정 후 사진 하나일 때 형변환 에러 해결을 위해
        var postImgLocations =
            if (post.postImgLocations.javaClass.toString() == "class java.util.Collections\$SingletonList")
                arrayListOf(post.postImgLocations[0])
            else
                post.postImgLocations as ArrayList<String>

        val intent: Intent = Intent(requireContext(), PhotoViewActivity::class.java)
        intent.putStringArrayListExtra(
            "imgs",
            postImgLocations
        )
        intent.putExtra("currentItem", position)
        startActivity(intent)
    }

}