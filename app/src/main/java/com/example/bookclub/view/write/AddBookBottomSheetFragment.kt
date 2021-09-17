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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AddBookBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddBookBottomSheetBinding
    private val bookViewModel: BookViewModel by activityViewModels()
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
            when(checkedId) {
                binding.readingRB.id -> readType = "NOW"
                binding.readCompleteRB.id -> readType = "AFTER"
                binding.wantToReadRB.id -> readType = "BEFORE"
            }
        }

        //책 추가하기 버튼 클릭 리스너 -> 서버에 책 추가 요청 보내기
        binding.addBtn.setOnClickListener {
            var code = runBlocking {
                withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                    bookViewModel.createBook(readType)
                }
            }

            if (code==201) {
                Log.e(code.toString(), "서버에 책 추가 완료!", )
                //완독, 읽는중 -> 기록하기 화면
                //읽고 싶은 -> 책 선택하기 화면
            } else {
                Log.e(code.toString(), "책 추가 서버에 실패!", )
            }
        }

        return binding.root
    }

}