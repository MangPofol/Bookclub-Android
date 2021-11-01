package com.mangpo.bookclub.view.book_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.mangpo.bookclub.databinding.FragmentSetGenderAndBirthBinding

class SetGenderAndBirthFragment : Fragment() {

    private lateinit var binding: FragmentSetGenderAndBirthBinding

    private val yearList = (1921..2021).toList().map { it.toString() }
    private val monthList = (1..12).toList().map { it.toString() }
    private var dayList = (1..31).toList().map { it.toString() }

    private val monthNpValueListener = NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->
        binding.dayNp.maxValue= 0

        dayList = if (listOf<String>("1", "3", "5", "7", "8", "10", "12").contains(dayList[newVal]))
            (1..31).toList().map { it.toString() }
        else if (listOf<String>("4", "6", "9", "11").contains(dayList[newVal]))
            (1..30).toList().map { it.toString() }
        else {
            val year = yearList[binding.yearNp.value].toInt()
            if (year%4==0 && year%100!=0 || year%400==0)
                (1..29).toList().map { it.toString() }
            else
                (1..28).toList().map { it.toString() }
        }
        binding.dayNp.displayedValues = dayList.toTypedArray()
        binding.dayNp.maxValue= dayList.size - 1
    }

    private val dayNpValueListener = NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->
        binding.dayNp.maxValue= 0

        val year = yearList[newVal].toInt()
        val month = monthList[binding.monthNp.value].toInt()

        dayList = if ((month==2) && (year%4==0 && year%100!=0 || year%400==0))
            (1..29).toList().map { it.toString() }
        else
            (1..28).toList().map { it.toString() }

        binding.dayNp.displayedValues = dayList.toTypedArray()
        binding.dayNp.maxValue= dayList.size - 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetGenderAndBirthBinding.inflate(inflater, container, false)

        (activity as BookProfileInitActivity).invisibleSkipTV()
        (activity as BookProfileInitActivity).unEnableNextBtn()
        (activity as BookProfileInitActivity).setKeyboardStatePan()

        binding.setGenderRg.setOnCheckedChangeListener { group, checkedId ->
            (activity as BookProfileInitActivity).enableNextBtn()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initDateNumberPicker()
    }

    private fun initDateNumberPicker() {
        binding.yearNp.displayedValues = yearList.toTypedArray()
        binding.yearNp.minValue = 0
        binding.yearNp.maxValue= yearList.size - 1
        binding.yearNp.wrapSelectorWheel = true
        binding.yearNp.setOnValueChangedListener(dayNpValueListener)

        binding.monthNp.displayedValues = monthList.toTypedArray()
        binding.monthNp.minValue = 0
        binding.monthNp.maxValue= monthList.size - 1
        binding.monthNp.wrapSelectorWheel = true
        binding.monthNp.setOnValueChangedListener(monthNpValueListener)

        binding.dayNp.displayedValues = dayList.toTypedArray()
        binding.dayNp.minValue = 0
        binding.dayNp.maxValue= dayList.size - 1
        binding.dayNp.wrapSelectorWheel = true
    }

}