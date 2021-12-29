package com.mangpo.bookclub.view.bookclub

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentHotMemoTopicBinding

//베타 버전 출시 후 사용
class HotMemoTopicFragment(val type: String) : Fragment() {
    private val hotMemo: ArrayList<String> = arrayListOf("메모 제목", "메모 제목", "메모 제목")
    private val hotMemoBook: ArrayList<String> = arrayListOf("나미야 잡화점의 기적", "나미야 잡화점의 기적", "나미야 잡화점의 기적")
    private val hotTopic: ArrayList<String> = arrayListOf("토픽 제목", "토픽 제목", "토픽 제목")
    private val hotTopicBook: ArrayList<String> = arrayListOf("공간의 미래", "공간의 미래", "공간의 미래")

    private lateinit var binding: FragmentHotMemoTopicBinding
    private lateinit var memoTopicTV: ArrayList<TextView>
    private lateinit var bookTV: ArrayList<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHotMemoTopicBinding.inflate(inflater, container, false)

        memoTopicTV = arrayListOf(binding.memoTopicTitleTv1, binding.memoTopicTitleTv2, binding.memoTopicTitleTv3)
        bookTV = arrayListOf(binding.bookTitleTv1, binding.bookTitleTv2, binding.bookTitleTv3)

        when(type) {
            "MEMO" -> {
                if (hotMemo.size > 0)
                    setMemoUI()
            }
            "TOPIC" -> {
                if (hotTopic.size > 0)
                    setTopicUI()
            }
        }

        return binding.root
    }

    private fun setTopicUI() {
        for (i in 0 until hotTopic.size) {
            memoTopicTV[i].text = hotTopic[i]
            bookTV[i].text = hotTopicBook[i]
        }
    }

    private fun setMemoUI() {
        for (i in 0 until hotMemo.size) {
            memoTopicTV[i].text = hotMemo[i]
            bookTV[i].text = hotMemoBook[i]
        }
    }

}