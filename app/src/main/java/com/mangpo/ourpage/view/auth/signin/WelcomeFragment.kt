package com.mangpo.ourpage.view.auth.signin

import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.FragmentWelcomeBinding
import com.mangpo.ourpage.view.BaseFragment

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(FragmentWelcomeBinding::inflate) {
    override fun initAfterBinding() {
        (requireActivity() as SignInActivity).changeToolbarText(null, null, getString(R.string.msg_input_information_display_my_profile))
    }

}