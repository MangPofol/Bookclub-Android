package com.mangpo.bookclub.view.auth.signin

import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentWelcomeBinding
import com.mangpo.bookclub.view.BaseFragment

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(FragmentWelcomeBinding::inflate) {
    override fun initAfterBinding() {
        (requireActivity() as SignInActivity).changeToolbarText(null, null, getString(R.string.msg_input_information_display_my_profile))
    }

}