package com.example.bookclub.view.library

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.bookclub.R
import com.example.bookclub.databinding.FragmentSortFilterBinding
import com.example.bookclub.model.BookModel
import com.example.bookclub.view.adapter.BookAdapter
import com.example.bookclub.viewmodel.BookViewModel
import com.example.bookclub.viewmodel.MyLibraryViewModel

class SortFilterFragment() : Fragment() {
    private lateinit var binding: FragmentSortFilterBinding

    private val myLibraryViewModel: MyLibraryViewModel by activityViewModels<MyLibraryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSortFilterBinding.inflate(inflater, container, false)

        //체크박스 체크 리스너
        val sortFilterListener = CompoundButton.OnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked)
                setCheckedState(checkBox.id)
            else
                isAllNotChecked()
        }

        //체크박스 리스너 등록
        binding.latestOrder.setOnCheckedChangeListener(sortFilterListener)
        binding.oldOrder.setOnCheckedChangeListener(sortFilterListener)
        binding.nameOrder.setOnCheckedChangeListener(sortFilterListener)

        return binding.root
    }

    private fun setCheckedState(checkBoxId: Int) {
        when (checkBoxId) {
            binding.latestOrder.id -> {
                myLibraryViewModel.updateSortFilter(0)

                binding.oldOrder.isChecked = false
                binding.nameOrder.isChecked = false
            }
            binding.oldOrder.id -> {
                myLibraryViewModel.updateSortFilter(1)

                binding.latestOrder.isChecked = false
                binding.nameOrder.isChecked = false
            }
            binding.nameOrder.id -> {
                myLibraryViewModel.updateSortFilter(2)

                binding.latestOrder.isChecked = false
                binding.oldOrder.isChecked = false
            }
        }
    }

    private fun isAllNotChecked() {
        if (!binding.latestOrder.isChecked&&!binding.oldOrder.isChecked&&!binding.nameOrder.isChecked) {
            myLibraryViewModel.updateSortFilter(-1)
        }
    }
}