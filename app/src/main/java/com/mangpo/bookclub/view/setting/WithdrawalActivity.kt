package com.mangpo.bookclub.view.setting

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityWithdrawalBinding
import com.mangpo.bookclub.utils.AuthUtils
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.utils.isNetworkAvailable
import com.mangpo.bookclub.view.BaseActivity
import com.mangpo.bookclub.view.auth.LoginActivity
import com.mangpo.bookclub.viewmodel.AuthViewModel
import com.mangpo.bookclub.viewmodel.UserViewModel

class WithdrawalActivity : BaseActivity<ActivityWithdrawalBinding>(ActivityWithdrawalBinding::inflate) {
    private val authVm: AuthViewModel by viewModels<AuthViewModel>()
    private val userVm: UserViewModel by viewModels<UserViewModel>()

    private var userId: Int? = null

    override fun initAfterBinding() {
        observe()

        if (!isNetworkAvailable(this))
            Snackbar.make(binding.root, getString(R.string.error_check_network), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.action_retry)) { getInfo() }
        else
            getInfo()

        binding.withdrawalBtn.setOnClickListener {
            authVm.changeDormant(userId!!)
        }
    }

    private fun getInfo() {
        userVm.getTotalMemoCnt()
        userVm.getUserInfo()
    }

    private fun observe() {
        userVm.totalMemoCnt.observe(this, Observer {
            LogUtil.d("WithdrawalActivity", "totalMemoCnt Observe! totalMemoCnt -> $it")
            binding.withdrawalTv.text = "Our Page를 떠나면\n${it}개 기록들이 사라져요."
        })

        userVm.getUserCode.observe(this, Observer {
            val code = it.getContentIfNotHandled()
            LogUtil.d("WithdrawalActivity", "getUserCode Observe! getUserCode -> $code")

            if (code!=null) {
                when (code) {
                    200 -> userId = userVm.getUser()!!.userId
                    else -> Snackbar.make(binding.root, getString(R.string.error_api), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.action_retry)) {
                        getInfo()
                    }
                }
            }
        })

        authVm.changeDormantCode.observe(this, Observer {
            LogUtil.d("WithdrawalActivity", "changeDormantCode Observe! changeDormantCode -> $it")

            if (it==204) {
                showToast(getString(R.string.msg_success_quit_membership))
                AuthUtils.clear()
                startActivityWithClear(LoginActivity::class.java)
            } else
                showSnackBar(getString(R.string.error_api))
        })
    }
}