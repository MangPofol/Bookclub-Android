package com.example.bookclub.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.bookclub.MainActivity
import com.example.bookclub.R
import com.example.bookclub.databinding.FragmentBookClubBinding

class BookClubFragment : Fragment() {
    private lateinit var binding: FragmentBookClubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookClubBinding.inflate(inflater, container, false)

        binding.hotContents.check(binding.hotMemoButton.id)   //처음 선택된 라디오버튼: 핫한 메모
        //라디오버튼 체크 이벤트 리스너
        binding.hotContents.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.hotMemoButton.id -> {   //핫한 메모 체크됐을 때
                    for (index in 0 until binding.hotMemoContents.childCount) {
                        (binding.hotMemoContents.getChildAt(index) as TextView).isEnabled = true
                        (binding.hotTopicContents.getChildAt(index) as TextView).isEnabled = false
                    }
                }
                binding.hotTopicButton.id -> {  //핫한 토픽 체크됐을 때
                    for (index in 0 until binding.hotTopicContents.childCount) {
                        (binding.hotTopicContents.getChildAt(index) as TextView).isEnabled = true
                        (binding.hotMemoContents.getChildAt(index) as TextView).isEnabled = false
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setDrawer(binding.toolbar)   //navigation drawer 등록
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_more_vert_36_white)  //navigation icon 설정
    }

}