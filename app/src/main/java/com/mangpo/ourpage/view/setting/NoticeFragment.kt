package com.mangpo.ourpage.view.setting

import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.FragmentNoticeBinding
import com.mangpo.ourpage.view.BaseFragment

class NoticeFragment : BaseFragment<FragmentNoticeBinding>(FragmentNoticeBinding::inflate) {
    override fun initAfterBinding() {
        (requireActivity() as SettingActivity).changeToolbarText(getString(R.string.title_notice), false)
    }
}