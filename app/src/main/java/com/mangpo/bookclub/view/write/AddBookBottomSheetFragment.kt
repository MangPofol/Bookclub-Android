package com.mangpo.bookclub.view.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentAddBookBottomSheetBinding

class AddBookBottomSheetFragment(val callback: (String) -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddBookBottomSheetBinding

    private var readType: String = "NOW"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //bottom sheet 모서리 둥글게 디자인
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_Demo_BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBookBottomSheetBinding.inflate(inflater, container, false)

        binding.readTypeRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.readingRB.id -> readType = "NOW"
                binding.readCompleteRB.id -> readType = "AFTER"
                binding.wantToReadRB.id -> readType = "BEFORE"
            }
        }

        //책 추가하기 버튼 클릭 리스너 -> 서버에 책 추가 요청 보내기
        binding.addBtn.setOnClickListener {
            dismiss()
            callback(readType)
        }

        return binding.root
    }

}