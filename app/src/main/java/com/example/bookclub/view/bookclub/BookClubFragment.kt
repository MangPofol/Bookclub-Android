package com.example.bookclub.view.bookclub

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import com.example.bookclub.view.MainActivity
import com.example.bookclub.R
import com.example.bookclub.databinding.FragmentBookClubBinding

class BookClubFragment : Fragment() {
    private lateinit var binding: FragmentBookClubBinding
    private lateinit var bottomSheet: ClubSelectBottomSheetFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookClubBinding.inflate(inflater, container, false)
        bottomSheet = ClubSelectBottomSheetFragment()

        //클럽 변경 시 bottom sheet dialog 띄우기
        binding.toolbar.setOnMenuItemClickListener {
            bottomSheet.show(
                (activity as MainActivity).supportFragmentManager, bottomSheet.tag)

            false
        }

        binding.hotContents.check(binding.hotMemoButton.id)   //처음 선택된 라디오버튼: 핫한 메모
        //라디오버튼 체크 이벤트 리스너
        binding.hotContents.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {

            }
        }

        //필터 체크박스 리스너
        val checkBoxListener = CompoundButton.OnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                changeFilterCheckBox(checkBox.id)
            }
        }

        //메인 필터 체크박스 리스너 등록
        binding.searchButton.setOnCheckedChangeListener(checkBoxListener)
        binding.clubMemberButton.setOnCheckedChangeListener(checkBoxListener)
        binding.sortButton.setOnCheckedChangeListener(checkBoxListener)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setDrawer(binding.toolbar)   //navigation drawer 등록
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_more_vert_36_white)  //navigation icon 설정
    }

    //체크된 필터 말고는 모두 체크 해제
    private fun changeFilterCheckBox(checkBoxId: Int) {
        when (checkBoxId) {
            binding.searchButton.id -> {
                binding.clubMemberButton.isChecked = false
                binding.sortButton.isChecked = false
            }
            binding.clubMemberButton.id -> {
                binding.searchButton.isChecked = false
                binding.sortButton.isChecked = false
            }
            binding.sortButton.id -> {
                binding.searchButton.isChecked = false
                binding.clubMemberButton.isChecked = false
            }
        }
    }

}