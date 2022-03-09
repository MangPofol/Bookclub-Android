package com.mangpo.bookclub.view.setting

import androidx.navigation.fragment.findNavController
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentSettingBinding
import com.mangpo.bookclub.utils.AuthUtils
import com.mangpo.bookclub.utils.isNetworkAvailable
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.auth.LoginActivity

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