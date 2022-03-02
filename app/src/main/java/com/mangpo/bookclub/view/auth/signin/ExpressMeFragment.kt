package com.mangpo.bookclub.view.auth.signin

import android.text.Editable
import android.text.TextWatcher
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentExpressMeBinding
import com.mangpo.bookclub.view.BaseFragment

class ExpressMeFragment : BaseFragment<FragmentExpressMeBinding>(FragmentExpressMeBinding::inflate), TextWatcher {
    override fun initAfterBinding() {
        (requireActivity() as SignInActivity).changeToolbarText(null, getString(R.string.action_skip), getString(R.string.msg_input_information_display_my_profile))
        validate()

        binding.expressMeEt.addTextChangedListener(this)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        validate()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    fun getData(): String? {
        val express = binding.expressMeEt.text.toString()

        return if (express.isBlank())
            null
        else
            express
    }

    private fun validate() {
        if (binding.expressMeEt.text.toString().isBlank())
            (requireActivity() as SignInActivity).changeNextButtonState(false)
        else
            (requireActivity() as SignInActivity).changeNextButtonState(true)
    }
}