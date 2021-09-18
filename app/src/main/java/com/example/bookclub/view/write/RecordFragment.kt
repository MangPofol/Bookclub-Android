package com.example.bookclub.view.write

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.bookclub.R
import com.example.bookclub.databinding.FragmentRecordBinding
import com.example.bookclub.model.KakaoBookModel
import com.example.bookclub.view.MainActivity
import com.example.bookclub.viewmodel.BookViewModel

class RecordFragment : Fragment() {
    private lateinit var binding: FragmentRecordBinding
    private val bookViewModel: BookViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordBinding.inflate(inflater, container, false)

        //selectedBook observer
        bookViewModel.selectedBook.observe(viewLifecycleOwner, Observer {
            if (it.title=="") { //빈값이면 책 선택 버튼에 "기록할 책을 선택하세요"
                binding.selectBookBtn.text = getString(R.string.book_select)
            } else {    //값이 존재하면 책 선택 버튼에 추가된 책의 이름
                binding.selectBookBtn.text = it.title
            }
        })

        binding.selectBookBtn.setOnClickListener {  //책 선택 버튼을 누르면 SelectBookFragment로 이동
            (parentFragment as WriteFragment).moveToSelectBook()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setDrawer(binding.toolbar)   //navigation drawer 등록
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_more_vert_36_black)  //navigation icon 설정
    }
}