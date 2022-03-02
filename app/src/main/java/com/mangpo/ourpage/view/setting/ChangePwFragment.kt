package com.mangpo.ourpage.view.setting

import android.view.View
import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.FragmentChangePwBinding
import com.mangpo.ourpage.model.entities.ChangePasswordReq
import com.mangpo.ourpage.utils.AuthUtils
import com.mangpo.ourpage.view.BaseFragment

class ChangePwFragment : BaseFragment<FragmentChangePwBinding>(FragmentChangePwBinding::inflate) {
    override fun initAfterBinding() {
        (requireActivity() as SettingActivity).changeToolbarText(getString(R.string.title_change_password), true)
    }

    fun validate(): ChangePasswordReq? {
        var changePwReq: ChangePasswordReq? = null

        if (binding.changePwOldPasswordEt.text.toString().isBlank())
            binding.changePwErrorTv.text = getString(R.string.error_input_old_pw)
        else if (binding.changePwNewPasswordEt.text.toString().isBlank())
            binding.changePwErrorTv.text = getString(R.string.error_input_new_pw)
        else if (binding.changePwNewAgainPasswordEt.text.toString().isBlank())
            binding.changePwErrorTv.text = getString(R.string.error_input_new_again_pw)
        else if (binding.changePwOldPasswordEt.text.toString()!=AuthUtils.getPassword())
            binding.changePwErrorTv.text = getString(R.string.error_wrong_old_pw)
        else if (binding.changePwNewPasswordEt.text.toString().length<6 || binding.changePwNewPasswordEt.text.toString().length>12)
            binding.changePwErrorTv.text = getString(R.string.error_not_use_password)
        else if (binding.changePwNewPasswordEt.text.toString() != binding.changePwNewAgainPasswordEt.text.toString())
            binding.changePwErrorTv.text = getString(R.string.error_different_new_pw)
        else
            changePwReq = ChangePasswordReq(binding.changePwNewPasswordEt.text.toString())

        if (changePwReq!=null) {
            binding.changePwErrorTv.visibility = View.INVISIBLE
        } else
            binding.changePwErrorTv.visibility = View.VISIBLE

        return changePwReq
    }
}