package com.mangpo.bookclub.view.book_profile

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentReadingStyleBinding

class ReadingStyleFragment : Fragment(), TextWatcher {

    private lateinit var binding: FragmentReadingStyleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReadingStyleBinding.inflate(inflater, container, false)

        (activity as BookProfileInitActivity).unEnableNextBtn()
        (activity as BookProfileInitActivity).visibleSkipTV()
        (activity as BookProfileInitActivity).setKeyboardStatePan()

        binding.styleRg.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId==-1) {
                Log.d("ReadingStyleFragment", "라디오버튼 미선택")

                judgeIsEnabledNextBtn()
            } else {
                Log.d("ReadingStyleFragment", "라디오버튼 선택")

                val imm: InputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.styleEt.windowToken, 0)

                binding.styleEt.clearFocus()

                (activity as BookProfileInitActivity).enableNextBtn()
            }
        }

        binding.styleEt.setOnFocusChangeListener { v, hasFocus ->
            Log.d("ReadingStyleFragment", hasFocus.toString())

            judgeIsEnabledNextBtn()

            if (hasFocus) {
                binding.styleRg.clearCheck()
                binding.styleEt.background = ContextCompat.getDrawable(requireContext(), R.drawable.reading_profile_style_et_blue)
                binding.styleEt.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            } else {
                binding.styleEt.background = ContextCompat.getDrawable(requireContext(), R.drawable.login_book_profile_et)
                binding.styleEt.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey12))
            }
        }

        binding.styleEt.addTextChangedListener(this)

        return binding.root
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (count>0)
            (activity as BookProfileInitActivity).enableNextBtn()
        else
            (activity as BookProfileInitActivity).unEnableNextBtn()
    }

    override fun afterTextChanged(s: Editable?) {

    }

    private fun judgeIsEnabledNextBtn() {
        if (binding.styleEt.text.isBlank()) {
            Log.d("ReadingStyleFragment", "비어 있다.")
            (activity as BookProfileInitActivity).unEnableNextBtn()
        } else {
            Log.d("ReadingStyleFragment", "비어 있지 않다.")
            (activity as BookProfileInitActivity).enableNextBtn()
        }
    }

    fun getReadingStyle(): String? {
        if (binding.styleRg.checkedRadioButtonId==-1) {
            return binding.styleEt.text.toString()
        } else {
            when (binding.styleRg.checkedRadioButtonId) {
                binding.styleRb1.id -> return binding.styleRb1.text.toString()
                binding.styleRb2.id -> return binding.styleRb2.text.toString()
                binding.styleRb3.id -> return binding.styleRb3.text.toString()
            }
        }

        return null
    }

}