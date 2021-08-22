package com.example.bookclub.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.example.bookclub.R
import com.example.bookclub.databinding.FragmentSortFilterBinding

class SortFilterFragment : Fragment() {
    private lateinit var binding: FragmentSortFilterBinding

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
        }

        //체크박스 리스너 등록
        binding.latestOrder.setOnCheckedChangeListener(sortFilterListener)
        binding.oldOrder.setOnCheckedChangeListener(sortFilterListener)
        binding.nameOrder.setOnCheckedChangeListener(sortFilterListener)

        return binding.root
    }

    private fun setCheckedState(checkBoxId: Int) {
        when (checkBoxId) {
            R.id.latestOrder -> {
                binding.oldOrder.isChecked = false
                binding.nameOrder.isChecked = false
            }
            R.id.oldOrder -> {
                binding.latestOrder.isChecked = false
                binding.nameOrder.isChecked = false
            }
            R.id.nameOrder -> {
                binding.latestOrder.isChecked = false
                binding.oldOrder.isChecked = false
            }
        }
    }
}