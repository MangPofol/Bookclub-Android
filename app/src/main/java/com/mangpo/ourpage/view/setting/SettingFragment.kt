package com.mangpo.ourpage.view.setting

import androidx.navigation.fragment.findNavController
import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.FragmentSettingBinding
import com.mangpo.ourpage.utils.AuthUtils
import com.mangpo.ourpage.utils.isNetworkAvailable
import com.mangpo.ourpage.view.BaseFragment
import com.mangpo.ourpage.view.auth.LoginActivity

class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {
    override fun initAfterBinding() {
        setMyEventListener()
    }

    private fun setMyEventListener() {
        binding.settingChangePwTv.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_changePwFragment)
        }

        binding.settingNoticeTv.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_noticeFragment)
        }

        binding.settingQuestionTv.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_questionFragment)
        }

        binding.settingTermsTv.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_termsFragment)
        }

        binding.settingOpenSourceLicenseTv.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_openSourceFragment)
        }

        binding.settingLogoutTv.setOnClickListener {
            if (!isNetworkAvailable(requireContext()))
                showNetworkSnackBar()
            else {
                AuthUtils.clear()
                (requireActivity() as SettingActivity).startActivityWithClear(LoginActivity::class.java)
            }
        }

        binding.settingQuitMembershipTv.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_withdrawalActivity)
        }
    }
}