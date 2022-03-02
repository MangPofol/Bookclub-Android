package com.mangpo.ourpage.view.setting

import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.ActivityWithdrawalBinding
import com.mangpo.ourpage.utils.AuthUtils
import com.mangpo.ourpage.utils.isNetworkAvailable
import com.mangpo.ourpage.view.BaseActivity
import com.mangpo.ourpage.view.auth.LoginActivity
import com.mangpo.ourpage.viewmodel.AuthViewModel
import com.mangpo.ourpage.viewmodel.UserViewModel

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
            Log.d("WithdrawalActivity", "totalMemoCnt Observe! totalMemoCnt -> $it")
            binding.withdrawalTv.text = "Our Page를 떠나면\n${it}개 기록들이 사라져요."
        })

        userVm.getUserCode.observe(this, Observer {
            val code = it.getContentIfNotHandled()
            Log.d("WithdrawalActivity", "getUserCode Observe! getUserCode -> $code")

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
            Log.d("WithdrawalActivity", "changeDormantCode Observe! changeDormantCode -> $it")

            if (it==204) {
                showToast(getString(R.string.msg_success_quit_membership))
                AuthUtils.clear()
                startActivityWithClear(LoginActivity::class.java)
            } else
                showSnackBar(getString(R.string.error_api))
        })
    }
}