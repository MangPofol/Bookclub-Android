package com.mangpo.bookclub.view.setting

import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentTermsBinding
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.bottomsheet.TermsBottomSheetFragment

class TermsFragment : BaseFragment<FragmentTermsBinding>(FragmentTermsBinding::inflate) {
    override fun initAfterBinding() {
        (requireActivity() as SettingActivity).changeToolbarText(getString(R.string.title_terms), false)

        binding.termsTermsOfServiceTv.setOnClickListener {
            val termsBottomSheetFragment: TermsBottomSheetFragment = TermsBottomSheetFragment("service")
            termsBottomSheetFragment.show(requireActivity().supportFragmentManager, null)
        }

        binding.termsPrivacyPolicyTv.setOnClickListener {
            val termsBottomSheetFragment: TermsBottomSheetFragment = TermsBottomSheetFragment("privacy")
            termsBottomSheetFragment.show(requireActivity().supportFragmentManager, null)
        }

        binding.termsReceiveMarketingInformationTv.setOnClickListener {
            val termsBottomSheetFragment: TermsBottomSheetFragment = TermsBottomSheetFragment("marketing")
            termsBottomSheetFragment.show(requireActivity().supportFragmentManager, null)
        }
    }
}