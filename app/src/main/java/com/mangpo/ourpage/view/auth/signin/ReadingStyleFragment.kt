package com.mangpo.ourpage.view.auth.signin

import android.text.Editable
import android.text.TextWatcher
import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.FragmentReadingStyleBinding
import com.mangpo.ourpage.view.BaseFragment

class ReadingStyleFragment : BaseFragment<FragmentReadingStyleBinding>(FragmentReadingStyleBinding::inflate), TextWatcher {
    override fun initAfterBinding() {
        (requireActivity() as SignInActivity).changeToolbarText(null, getString(R.string.action_skip), getString(R.string.msg_input_information_display_my_profile))
        validate()
        setMyEventClickListener()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when {
            p0?.length!=0 -> binding.styleRb4.isChecked = true
            binding.readingStyleRg.checkedRadioButtonId!=-1 -> binding.styleRb4.isChecked = false
            else -> {
                binding.styleRb4.isChecked = false
                binding.readingStyleRg.clearCheck()
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {
        validate()
    }

    private fun setMyEventClickListener() {
        binding.readingStyleRg.setOnCheckedChangeListener { radioGroup, i ->
            when (radioGroup.checkedRadioButtonId) {
                binding.styleRb1.id, binding.styleRb2.id, binding.styleRb3.id -> {
                    hideKeyboard()

                    binding.styleEt.setText("")
                    binding.styleEt.clearFocus()
                }
            }

            validate()
        }

        binding.styleEt.addTextChangedListener(this)
    }

    private fun validate() {
        if (binding.readingStyleRg.checkedRadioButtonId==-1 && binding.styleEt.text.isBlank())
            (requireActivity() as SignInActivity).changeNextButtonState(false)
        else
            (requireActivity() as SignInActivity).changeNextButtonState(true)

    }

    fun getData(): String? {
        return when (binding.readingStyleRg.checkedRadioButtonId) {
            binding.styleRb1.id -> binding.styleRb1.text.toString()
            binding.styleRb2.id -> binding.styleRb2.text.toString()
            binding.styleRb3.id -> binding.styleRb3.text.toString()
            binding.styleRb4.id -> binding.styleEt.text.toString()
            else -> null
        }
    }
}