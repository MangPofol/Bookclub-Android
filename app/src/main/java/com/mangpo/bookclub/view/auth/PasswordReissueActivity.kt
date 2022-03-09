package com.mangpo.bookclub.view.auth

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityPasswordReissueBinding
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.utils.isNetworkAvailable
import com.mangpo.bookclub.view.BaseActivity
import com.mangpo.bookclub.view.dialog.TempPwDialogFragment
import com.mangpo.bookclub.viewmodel.AuthViewModel
import java.util.regex.Pattern

class PasswordReissueActivity : BaseActivity<ActivityPasswordReissueBinding>(ActivityPasswordReissueBinding::inflate), TextWatcher {
    private val authVm: AuthViewModel by viewModels<AuthViewModel>()
    private val emailPattern: Pattern = Patterns.EMAIL_ADDRESS

    override fun initAfterBinding() {
        setMyEventListener()
        observe()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (!emailPattern.matcher(binding.passwordReissueEmailEt.text.toString()).matches()) {
            binding.passwordReissueEmailErrorTv.text = getString(R.string.error_not_email_pattern)
            binding.passwordReissueEmailErrorTv.visibility = View.VISIBLE
        } else
            authVm.validateDuplicate(p0.toString())
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun setMyEventListener() {
        binding.passwordReissueEmailEt.addTextChangedListener(this)

        binding.passwordReissueSendBtn.setOnClickListener {
            hideKeyboard(binding.root)

            if (!isNetworkAvailable(this))
                showSnackBar(getString(R.string.error_check_network))
            else {
                showLoadingDialog()
                authVm.lostPassword(binding.passwordReissueEmailEt.text.toString())
            }
        }
    }

    private fun observe() {
        authVm.validateCode.observe(this, Observer {
            LogUtil.d("PasswordReissueActivity", "validateCode Observe! -> validateCode: $it")

            if (it == 204) {
                binding.passwordReissueEmailErrorTv.apply {
                    text = getString(R.string.error_not_registered_email)
                    visibility = View.VISIBLE
                }
                binding.passwordReissueSendBtn.isEnabled = false
            } else {
                binding.passwordReissueEmailErrorTv.visibility = View.INVISIBLE
                binding.passwordReissueSendBtn.isEnabled = true
            }
        })

        authVm.lostPasswordCode.observe(this, Observer {
            dismissLoadingDialog()

            val code = it.getContentIfNotHandled()
            LogUtil.d("PasswordReissueActivity", "lostPasswordCode Observe! -> lostPasswordCode: $code")

            if (code !=null && code==204)
                TempPwDialogFragment().show(supportFragmentManager, null)
            else if (code!=null && code!=204)
                showSnackBar(getString(R.string.error_api))
        })
    }
}