package com.example.bookclub.view.write

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.bookclub.R
import com.example.bookclub.databinding.FragmentAddBookBottomSheetBinding
import com.example.bookclub.model.KakaoBookModel
import com.example.bookclub.viewmodel.BookViewModel
import com.example.bookclub.viewmodel.SelectedBookViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AddBookBottomSheetFragment(val callback: (String) -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddBookBottomSheetBinding

    private var readType: String = "NOW"

    private val selectedBookViewModel: SelectedBookViewModel by activityViewModels<SelectedBookViewModel>()


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
            when(checkedId) {
                binding.readingRB.id -> readType = "NOW"
                binding.readCompleteRB.id -> readType = "AFTER"
                binding.wantToReadRB.id -> readType = "BEFORE"
            }
        }

        //책 추가하기 버튼 클릭 리스너 -> 서버에 책 추가 요청 보내기
        binding.addBtn.setOnClickListener {
            Log.e("click", selectedBookViewModel.selectedBook.value.toString())
            when (readType) {
                "NOW", "AFTER" -> {
                    dismiss()
                    callback(readType)
                }
                "BEFORE" -> {

                }
            }
        }

        return binding.root
    }

}