package com.mangpo.bookclub.view.book_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentReadingGoalBinding

class ReadingGoalFragment : Fragment() {

    private lateinit var binding: FragmentReadingGoalBinding

    private val periodNumList = (1..12).toList().map { it.toString() }
    private val periodUnitList = listOf<String>("년", "개월", "일")
    private val bookCntList = (1..40).toList().map { it.toString() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReadingGoalBinding.inflate(inflater, container, false)

        (activity as BookProfileInitActivity).visibleSkipTV()

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initNumberPicker()
    }

    private fun initNumberPicker() {
        binding.periodNumberNp.displayedValues = periodNumList.toTypedArray()
        binding.periodNumberNp.minValue = 0
        binding.periodNumberNp.maxValue = periodNumList.size - 1
        binding.periodNumberNp.wrapSelectorWheel = true

        binding.periodUnitNp.displayedValues = periodUnitList.toTypedArray()
        binding.periodUnitNp.minValue = 0
        binding.periodUnitNp.maxValue = periodUnitList.size - 1
        binding.periodUnitNp.wrapSelectorWheel = true
        binding.periodUnitNp.value = 1
        binding.periodUnitNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        binding.bookCntNp.displayedValues = bookCntList.toTypedArray()
        binding.bookCntNp.minValue = 0
        binding.bookCntNp.maxValue = bookCntList.size - 1
        binding.bookCntNp.wrapSelectorWheel = true
    }
}