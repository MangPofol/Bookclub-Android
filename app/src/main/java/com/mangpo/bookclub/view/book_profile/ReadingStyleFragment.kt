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
            when (checkedId) {
                R.id.style_rb1, R.id.style_rb2, R.id.style_rb3 -> {
                    Log.d("ReadingStyleFragment", "라디오버튼 선택")

                    binding.styleEt.setText("")
                    binding.styleEt.clearFocus()

                    val imm: InputMethodManager =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.styleEt.windowToken, 0)

                    (activity as BookProfileInitActivity).enableNextBtn()
                }
                R.id.style_rb4 -> {
                    (activity as BookProfileInitActivity).enableNextBtn()
                }
            }
        }

        binding.styleEt.addTextChangedListener(this)

        return binding.root
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("ReadingStyleFragment", "onTextChanged length: ${s?.length}")
        if (s?.length != 0) {
            binding.styleRb4.isChecked = true
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    fun getReadingStyle(): String {
        return when (binding.styleRg.checkedRadioButtonId) {
            binding.styleRb1.id -> binding.styleRb1.text.toString()
            binding.styleRb2.id -> binding.styleRb2.text.toString()
            binding.styleRb3.id -> binding.styleRb3.text.toString()
            else -> binding.styleEt.text.toString()
        }
    }

}