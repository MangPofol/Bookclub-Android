package com.mangpo.bookclub.view.setting

import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivitySettingBinding
import com.mangpo.bookclub.utils.isNetworkAvailable
import com.mangpo.bookclub.view.BaseActivity
import com.mangpo.bookclub.viewmodel.AuthViewModel

class SettingActivity : BaseActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {
    private val authVm: AuthViewModel by viewModels<AuthViewModel>()

    override fun initAfterBinding() {
        setMyEventListener()
        observe()
    }

    private fun setMyEventListener() {
        binding.settingTb.setNavigationOnClickListener {
            if (findNavController(binding.settingNavHostFragment.id).currentDestination?.id == R.id.settingFragment)
                finish()
            else
                findNavController(binding.settingNavHostFragment.id).popBackStack()
        }

        binding.settingCompleteTv.setOnClickListener {
            when (findNavController(binding.settingNavHostFragment.id).currentDestination?.id) {
                R.id.changePwFragment -> {
                    val changePasswordReq = (supportFragmentManager.findFragmentById(binding.settingNavHostFragment.id)?.childFragmentManager?.fragments?.get(0) as ChangePwFragment).validate()

                    if (isNetworkAvailable(this) && changePasswordReq!=null)
                        authVm.changePassword(changePasswordReq)
                    else if (!isNetworkAvailable(this))
                        showSnackBar(getString(R.string.error_check_network))
                }
            }
        }
    }

    private fun observe() {
        authVm.changePasswordCode.observe(this, Observer {
            Log.d("SettingActivity", "changePasswordCode Observe! changePasswordCode -> $it")

            if (it==204) {
                showToast(getString(R.string.msg_success_change_pw))
                finish()
            } else
                showToast(getString(R.string.error_api))
        })
    }

    fun changeToolbarText(title: String, completeVisibility: Boolean) {
        binding.settingTb.title = title

        if (completeVisibility)
            binding.settingCompleteTv.visibility = View.VISIBLE
        else
            binding.settingCompleteTv.visibility = View.INVISIBLE
    }

}