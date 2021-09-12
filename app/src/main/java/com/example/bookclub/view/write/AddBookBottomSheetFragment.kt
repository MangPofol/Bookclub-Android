package com.example.bookclub.view.write

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.bookclub.R
import com.example.bookclub.databinding.FragmentAddBookBottomSheetBinding
import com.example.bookclub.viewmodel.BookViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddBookBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddBookBottomSheetBinding
    private val bookViewModel: BookViewModel by activityViewModels()

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

        binding.addBtn.setOnClickListener {
            Log.e("책 추가하기", bookViewModel.selectedBookTitle.value.toString())
        }

        return binding.root
    }

}