package com.mangpo.bookclub.view.auth.signin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentEmailAuthenticationBinding
import com.mangpo.bookclub.utils.AuthUtils
import com.mangpo.bookclub.utils.isNetworkAvailable
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.viewmodel.AuthViewModel

class EmailAuthenticationFragment : BaseFragment<FragmentEmailAuthenticationBinding>(FragmentEmailAuthenticationBinding::inflate), TextWatcher {
    private val authVm by activityViewModels<AuthViewModel>()

    override fun initAfterBinding() {
        (requireActivity() as SignInActivity).changeToolbarText(getString(R.string.title_email_authentication), getString(R.string.action_complete), null)

        val email = AuthUtils.getEmail()
        if (email.isNotBlank())
            binding.emailAuthenticationEmailEt.setText(email)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (email.isNotBlank()) {
                    requireActivity().finish()
                    AuthUtils.clear()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        validate()
        setMyEventListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        validate()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun validate() {
        if (binding.emailAuthenticationCodeEt.text.isBlank()) {
            (requireActivity() as SignInActivity).changeActionTextViewState(false)
            (requireActivity() as SignInActivity).changeNextButtonState(false)
        } else {
            (requireActivity() as SignInActivity).changeActionTextViewState(true)
            (requireActivity() as SignInActivity).changeNextButtonState(true)
        }
    }

    private fun setMyEventListener() {
        binding.emailAuthenticationCodeEt.addTextChangedListener(this)

        binding.emailAuthenticationSendBtn.setOnClickListener {
            if (!isNetworkAvailable(requireContext()))
                showNetworkSnackBar()
            else {
                binding.emailAuthenticationSendBtn.visibility = View.INVISIBLE
                binding.emailAuthenticationResendBtn.visibility = View.VISIBLE
                authVm.validateEmail()
            }
        }

        binding.emailAuthenticationResendBtn.setOnClickListener {
            if (!isNetworkAvailable(requireContext()))
                showNetworkSnackBar()
            else {
                binding.emailAuthenticationSendBtn.visibility = View.INVISIBLE
                binding.emailAuthenticationResendBtn.visibility = View.VISIBLE
                authVm.validateEmail()
            }
        }
    }

    private fun observe() {
        authVm.validateEmailCode.observe(viewLifecycleOwner, Observer {
            if (it!=204)
                showSnackBar(getString(R.string.error_api))
        })

        authVm.validateEmailSendCode.observe(viewLifecycleOwner, Observer {
            val code: Int? = it.getContentIfNotHandled()

            binding.emailAuthenticationCodeErrorTv.visibility = View.INVISIBLE
            if (code!=null) {
                when (code) {
                    204 -> findNavController().navigate(R.id.action_emailAuthenticationFragment_to_welcomeFragment)
                    400 -> binding.emailAuthenticationCodeErrorTv.visibility = View.VISIBLE
                    else -> showSnackBar(getString(R.string.error_api))
                }
            }
        })
    }

    fun getEmailCode(): Int = binding.emailAuthenticationCodeEt.text.toString().toInt()
}