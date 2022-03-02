package com.mangpo.bookclub.view.auth.signin

import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.lifecycle.Observer
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentEmailPasswordBinding
import com.mangpo.bookclub.model.entities.User
import com.mangpo.bookclub.utils.isNetworkAvailable
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.bottomsheet.TermsBottomSheetFragment
import com.mangpo.bookclub.viewmodel.AuthViewModel
import java.util.regex.Pattern

class EmailPasswordFragment :
    BaseFragment<FragmentEmailPasswordBinding>(FragmentEmailPasswordBinding::inflate), TextWatcher {
    private val authVm: AuthViewModel = AuthViewModel()
    private val emailPattern: Pattern = Patterns.EMAIL_ADDRESS

    private var isChecked: Boolean = false

    override fun initAfterBinding() {
        binding.emailPasswordTermsTv.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.emailPasswordPrivacyPolicyTv.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        (requireActivity() as SignInActivity).changeToolbarText(
            getString(R.string.action_signin),
            getString(R.string.action_complete),
            null
        )

        if (isChecked) {
            binding.emailPasswordUncheckedIv.visibility = View.INVISIBLE
            binding.emailPasswordCheckedIv.visibility = View.VISIBLE
        } else {
            binding.emailPasswordUncheckedIv.visibility = View.VISIBLE
            binding.emailPasswordCheckedIv.visibility = View.INVISIBLE
        }

        validate()

        if (!isNetworkAvailable(requireContext())) {
            showNetworkSnackBar()
        }

        setMyEventListener()
        observe()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (binding.emailPasswordEmailEt.hasFocus()) {    //아이디 중복확인
            if (!emailPattern.matcher(binding.emailPasswordEmailEt.text.toString()).matches()) {
                binding.emailPasswordEmailErrorTv.text = getString(R.string.error_not_email_pattern)
                binding.emailPasswordEmailErrorTv.visibility = View.VISIBLE
            } else
                authVm.validateDuplicate(p0.toString())
        } else if (binding.emailPasswordPasswordEt.hasFocus()) { //비밀번호 6~12자
            if (p0?.length in 6..12)
                binding.emailPasswordPasswordErrorTv.visibility = View.INVISIBLE
            else
                binding.emailPasswordPasswordErrorTv.visibility = View.VISIBLE

            validate()
        } else {    //비밀번호 6~12자 & 비밀번호, 비밀번호 확인 일치 여부
            if (binding.emailPasswordPasswordEt.text.toString() == p0.toString() && p0?.length in 6..12)
                binding.emailPasswordPasswordErrorTv.visibility = View.INVISIBLE
            else
                binding.emailPasswordPasswordErrorTv.visibility = View.VISIBLE

            validate()
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun validate() {
        if (binding.emailPasswordEmailErrorTv.visibility == View.VISIBLE || binding.emailPasswordPasswordErrorTv.visibility == View.VISIBLE || binding.emailPasswordEmailEt.text.isBlank() || binding.emailPasswordPasswordEt.text.isBlank() || binding.emailPasswordPasswordConfirmEt.text.isBlank() || binding.emailPasswordUncheckedIv.visibility==View.VISIBLE) {
            (requireActivity() as SignInActivity).changeNextButtonState(false)
            (requireActivity() as SignInActivity).changeActionTextViewState(false)
        }
        else {
            (requireActivity() as SignInActivity).changeNextButtonState(true)
            (requireActivity() as SignInActivity).changeActionTextViewState(true)
        }
    }

    private fun setMyEventListener() {
        binding.emailPasswordEmailEt.addTextChangedListener(this)
        binding.emailPasswordPasswordEt.addTextChangedListener(this)
        binding.emailPasswordPasswordConfirmEt.addTextChangedListener(this)

        binding.emailPasswordUncheckedIv.setOnClickListener {
            binding.emailPasswordCheckedIv.visibility = View.VISIBLE
            it.visibility = View.INVISIBLE

            isChecked = true

            validate()
        }

        binding.emailPasswordCheckedIv.setOnClickListener {
            binding.emailPasswordUncheckedIv.visibility = View.VISIBLE
            it.visibility = View.INVISIBLE

            isChecked = false

            (requireActivity() as SignInActivity).changeNextButtonState(false)
            (requireActivity() as SignInActivity).changeActionTextViewState(false)
        }

        binding.emailPasswordTermsTv.setOnClickListener {
            val termsBottomSheetFragment: TermsBottomSheetFragment = TermsBottomSheetFragment("service")
            termsBottomSheetFragment.show(requireActivity().supportFragmentManager, null)
        }

        binding.emailPasswordPrivacyPolicyTv.setOnClickListener {
            val termsBottomSheetFragment: TermsBottomSheetFragment = TermsBottomSheetFragment("privacy")
            termsBottomSheetFragment.show(requireActivity().supportFragmentManager, null)
        }
    }

    private fun observe() {
        authVm.validateCode.observe(this, Observer {
            if (it == 400) {
                binding.emailPasswordEmailErrorTv.text = getString(R.string.error_email_already_registered)
                binding.emailPasswordEmailErrorTv.visibility = View.VISIBLE
            } else {
                binding.emailPasswordEmailErrorTv.visibility = View.INVISIBLE
            }

            validate()
        })
    }

    fun getUser(): User = User(email = binding.emailPasswordEmailEt.text.toString(), password = binding.emailPasswordPasswordEt.text.toString())
}