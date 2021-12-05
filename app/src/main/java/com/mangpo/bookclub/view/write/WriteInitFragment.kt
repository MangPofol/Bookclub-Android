package com.mangpo.bookclub.view.write

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentWriteInitBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.view.SettingActivity
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class WriteInitFragment : Fragment() {

    private lateinit var binding: FragmentWriteInitBinding

    private val postVm: PostViewModel by sharedViewModel()
    private val bookVm: BookViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("WriteInitFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("WriteInitFragment", "onCreateView")

        binding = FragmentWriteInitBinding.inflate(layoutInflater, container, false)

        //햄버거 메뉴 클릭 리스너 -> SettingActivity 화면 이동
        binding.hamburgerIb.setOnClickListener {
            val intent: Intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }

        //날짜 표시하기
        val calendar: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("LLLL dd.", Locale.ENGLISH)
        binding.dateTv.text = "The record for ${sdf.format(calendar.time)}"

        //기록 쓰러가기 클릭 리스너 -> RecordFragment 로 이동
        binding.goPostView.setOnClickListener {
            (requireParentFragment() as WriteFrameFragment).moveToRecord(false)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("WriteInitFragment", "onResume")

        bookVm.setBook(BookModel())
        postVm.setPost(PostModel())
        postVm.setImgUriList(listOf())
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("WriteInitFragment", "onDetach")
    }

}