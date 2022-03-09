package com.mangpo.bookclub.view.setting

import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentNoticeBinding
import com.mangpo.bookclub.view.BaseFragment

class NoticeFragment : BaseFragment<FragmentNoticeBinding>(FragmentNoticeBinding::inflate) {
    override fun initAfterBinding() {
        (requireActivity() as SettingActivity).changeToolbarText(getString(R.string.title_notice), false)
    }
}