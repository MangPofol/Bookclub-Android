package com.mangpo.bookclub.view.auth.signin

import android.widget.NumberPicker
import com.mangpo.bookclub.databinding.FragmentGenderBirthBinding
import com.mangpo.bookclub.view.BaseFragment
import java.time.LocalDateTime

class GenderBirthFragment : BaseFragment<FragmentGenderBirthBinding>(FragmentGenderBirthBinding::inflate) {

    private val yearList = (1921..2022).toList().map { it.toString() }
    private val monthList = (1..12).toList().map { it.toString() }
    private var dayList = (1..31).toList().map { it.toString() }

    private val monthNpValueListener =
        NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->
            binding.genderBirthDayNp.maxValue = 0

            dayList =
                if (listOf<String>("1", "3", "5", "7", "8", "10", "12").contains(dayList[newVal]))
                    (1..31).toList().map { it.toString() }
                else if (listOf<String>("4", "6", "9", "11").contains(dayList[newVal]))
                    (1..30).toList().map { it.toString() }
                else {
                    val year = yearList[binding.genderBirthYearNp.value].toInt()
                    if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
                        (1..29).toList().map { it.toString() }
                    else
                        (1..28).toList().map { it.toString() }
                }
            binding.genderBirthDayNp.displayedValues = dayList.toTypedArray()
            binding.genderBirthDayNp.maxValue = dayList.size - 1
        }

    private val dayNpValueListener = NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->
        binding.genderBirthDayNp.maxValue = 0

        val year = yearList[newVal].toInt()
        val month = monthList[binding.genderBirthMonthNp.value].toInt()

        dayList = if ((month == 2) && (year % 4 == 0 && year % 100 != 0 || year % 400 == 0))
            (1..29).toList().map { it.toString() }
        else
            (1..28).toList().map { it.toString() }

        binding.genderBirthDayNp.displayedValues = dayList.toTypedArray()
        binding.genderBirthDayNp.maxValue = dayList.size - 1
    }

    override fun initAfterBinding() {
        (requireActivity() as SignInActivity).changeToolbarText(null, null, null)
        validate()
        setMyEventListener()
    }

    override fun onStart() {
        super.onStart()
        initDateNumberPicker()
    }

    private fun initDateNumberPicker() {
        binding.genderBirthYearNp.displayedValues = yearList.toTypedArray()
        binding.genderBirthYearNp.minValue = 0
        binding.genderBirthYearNp.maxValue = yearList.size - 1
        binding.genderBirthYearNp.value = 78
        binding.genderBirthYearNp.wrapSelectorWheel = true
        binding.genderBirthYearNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        binding.genderBirthYearNp.setOnValueChangedListener(dayNpValueListener)

        binding.genderBirthMonthNp.displayedValues = monthList.toTypedArray()
        binding.genderBirthMonthNp.minValue = 0
        binding.genderBirthMonthNp.maxValue = monthList.size - 1
        binding.genderBirthMonthNp.wrapSelectorWheel = true
        binding.genderBirthMonthNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        binding.genderBirthMonthNp.setOnValueChangedListener(monthNpValueListener)

        binding.genderBirthDayNp.displayedValues = dayList.toTypedArray()
        binding.genderBirthDayNp.minValue = 0
        binding.genderBirthDayNp.maxValue = dayList.size - 1
        binding.genderBirthDayNp.wrapSelectorWheel = true
        binding.genderBirthDayNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
    }

    private fun validate() {
        if (binding.genderBirthGenderRg.checkedRadioButtonId==-1) {
            (requireActivity() as SignInActivity).changeNextButtonState(false)
            (requireActivity() as SignInActivity).changeActionTextViewState(false)
        } else {
            (requireActivity() as SignInActivity).changeNextButtonState(true)
            (requireActivity() as SignInActivity).changeActionTextViewState(true)
        }
    }

    private fun setMyEventListener() {
        binding.genderBirthGenderRg.setOnCheckedChangeListener { radioGroup, i -> validate() }
    }

    fun getGender(): String {
        return when (binding.genderBirthGenderRg.checkedRadioButtonId) {
            binding.genderBirthManRb.id -> "MALE"
            else -> "FEMALE"
        }
    }

    fun getBirth(): String {
        val year = yearList[binding.genderBirthYearNp.value].toInt()
        val month = monthList[binding.genderBirthMonthNp.value].toInt()
        val day = dayList[binding.genderBirthDayNp.value].toInt()

        return LocalDateTime.of(year, month, day, 0, 0).toString()
    }
}