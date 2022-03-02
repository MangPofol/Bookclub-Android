package com.mangpo.ourpage.view.auth.signin

import android.widget.NumberPicker
import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.FragmentReadingGoalBinding
import com.mangpo.ourpage.view.BaseFragment

class ReadingGoalFragment : BaseFragment<FragmentReadingGoalBinding>(FragmentReadingGoalBinding::inflate) {
    private val periodNumList = (0..30).toList().map { it.toString() }
    private val periodUnitList = listOf<String>("년", "개월", "일")
    private val bookCntList = (0..100).toList().map { it.toString() }

    override fun initAfterBinding() {
        (requireActivity() as SignInActivity).changeToolbarText(null, getString(R.string.action_skip), getString(
            R.string.msg_input_information_display_my_profile
        ))
        validate()
    }

    override fun onStart() {
        super.onStart()
        initNumberPicker()
    }

    private fun initNumberPicker() {
        binding.readingGoalPeriodNumberNp.displayedValues = periodNumList.toTypedArray()
        binding.readingGoalPeriodNumberNp.minValue = 0
        binding.readingGoalPeriodNumberNp.maxValue = periodNumList.size - 1
        binding.readingGoalPeriodNumberNp.wrapSelectorWheel = true
        binding.readingGoalPeriodNumberNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        binding.readingGoalPeriodNumberNp.setOnValueChangedListener { numberPicker, i, i2 -> validate() }

        binding.readingGoalPeriodUnitNp.displayedValues = periodUnitList.toTypedArray()
        binding.readingGoalPeriodUnitNp.minValue = 0
        binding.readingGoalPeriodUnitNp.maxValue = periodUnitList.size - 1
        binding.readingGoalPeriodUnitNp.wrapSelectorWheel = true
        binding.readingGoalPeriodUnitNp.value = 1
        binding.readingGoalPeriodUnitNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        binding.readingGoalBookCntNp.displayedValues = bookCntList.toTypedArray()
        binding.readingGoalBookCntNp.minValue = 0
        binding.readingGoalBookCntNp.maxValue = bookCntList.size - 1
        binding.readingGoalBookCntNp.wrapSelectorWheel = true
        binding.readingGoalBookCntNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        binding.readingGoalBookCntNp.setOnValueChangedListener { numberPicker, i, i2 -> validate() }
    }

    private fun validate() {
        val number = periodNumList[binding.readingGoalPeriodNumberNp.value].toInt()
        val cnt = bookCntList[binding.readingGoalBookCntNp.value].toInt()

        if (number==0 && cnt==0)
            (requireActivity() as SignInActivity).changeNextButtonState(false)
        else
            (requireActivity() as SignInActivity).changeNextButtonState(true)
    }

    fun getData(): String = "${periodNumList[binding.readingGoalPeriodNumberNp.value]}${periodUnitList[binding.readingGoalPeriodUnitNp.value]} 동안 ${bookCntList[binding.readingGoalBookCntNp.value]}권의 책을 기록하기"
}