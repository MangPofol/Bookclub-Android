package com.mangpo.bookclub.view.book_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.mangpo.bookclub.databinding.FragmentReadingGoalBinding

class ReadingGoalFragment : Fragment() {

    private lateinit var binding: FragmentReadingGoalBinding

    private val periodNumList = (0..30).toList().map { it.toString() }
    private val periodUnitList = listOf<String>("년", "개월", "일")
    private val bookCntList = (0..100).toList().map { it.toString() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReadingGoalBinding.inflate(inflater, container, false)

        (activity as BookProfileInitActivity).visibleSkipTV()   //건너띄기 텍스트뷰 보이도록
        (activity as BookProfileInitActivity).unEnableNextBtn() //다음 버튼 비활성화

        //NumberPicker 값 변경되면 다음 버튼 활성화 시키기
        binding.periodNumberNp.setOnValueChangedListener { picker, oldVal, newVal ->
            (activity as BookProfileInitActivity).enableNextBtn()
        }
        binding.periodUnitNp.setOnValueChangedListener { picker, oldVal, newVal ->
            (activity as BookProfileInitActivity).enableNextBtn()
        }
        binding.bookCntNp.setOnValueChangedListener { picker, oldVal, newVal ->
            (activity as BookProfileInitActivity).enableNextBtn()
        }

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
        binding.periodNumberNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

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
        binding.bookCntNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
    }

    fun getGoal(): String =
        "${periodNumList[binding.periodNumberNp.value]}${periodUnitList[binding.periodUnitNp.value]} 동안 ${bookCntList[binding.bookCntNp.value]}권의 책을 기록하기"
}