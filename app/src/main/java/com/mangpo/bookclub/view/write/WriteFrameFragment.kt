package com.mangpo.bookclub.view.write

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.mangpo.bookclub.databinding.FragmentWriteFrameBinding
import com.mangpo.bookclub.model.BookModel

class WriteFrameFragment : Fragment() {
    private lateinit var binding: FragmentWriteFrameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("WriteFrameFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("WriteFrameFragment", "onCreateView")

        binding = FragmentWriteFrameBinding.inflate(inflater, container, false)

        //bundle 에 book 객체가 있으면 BookDescFragment 에서 온 거고 객체가 없으면 WritingSettingFragment 에서 온 것
        val bookStr = arguments?.getString("book")
        if (bookStr==null)
            childFragmentManager.beginTransaction().replace(binding.frameLayout.id, MainFragment())
                .addToBackStack(null).commitAllowingStateLoss()
        else {
            val bookObj = Gson().fromJson(bookStr, BookModel::class.java)
            moveToPostDetail(bookObj.id, bookObj.name)
            arguments?.remove("book")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("WriteFrameFragment", "onViewCreated")
    }

    override fun onResume() {
        super.onResume()
        Log.d("WriteFrameFragment", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("WriteFrameFragment", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("WriteFrameFragment", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("WriteFrameFragment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("WriteFrameFragment", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("WriteFrameFragment", "onDetach")
    }

    //RecordFragment 화면으로 이동하는 함수
    fun moveToRecord(isUpdate: Boolean) {
        childFragmentManager.beginTransaction().replace(binding.frameLayout.id, RecordFragment(isUpdate))
            .addToBackStack(null).commitAllowingStateLoss()
    }

    //SelectFragment 화면으로 이동하는 함수
    fun moveToSelect() {
        childFragmentManager.beginTransaction().replace(binding.frameLayout.id, SelectFragment())
            .addToBackStack(null).commitAllowingStateLoss()
    }

    //WritingSettingFragment 화면으로 이동하는 함수
    fun moveToWritingSetting(isUpdate: Boolean) {
        childFragmentManager.beginTransaction().replace(binding.frameLayout.id, WritingSettingFragment(isUpdate))
            .addToBackStack(null).commitAllowingStateLoss()
    }

    //PostDetailFragment 화면으로 이동하는 함수
    fun moveToPostDetail(bookId: Long?, bookName: String?) {
        Log.d("WriteFrameFragment", "moveToPostDetail -> ${this.id}")

        //bundle 을 활용해 bookId 와 bookName 을 PostDetailFragment 에 전달
        val fragment = PostDetailFragment()
        val bundle = Bundle()
        bundle.putLong("bookId", bookId!!)
        bundle.putString("bookName", bookName)
        fragment.arguments = bundle

        childFragmentManager.beginTransaction().replace(
            binding.frameLayout.id,
            fragment
        ).addToBackStack(null).commitAllowingStateLoss()
    }
}