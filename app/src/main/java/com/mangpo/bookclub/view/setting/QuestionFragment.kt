package com.mangpo.bookclub.view.setting

import android.content.Intent
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentQuestionBinding
import com.mangpo.bookclub.view.BaseFragment

class QuestionFragment : BaseFragment<FragmentQuestionBinding>(FragmentQuestionBinding::inflate) {
    override fun initAfterBinding() {
        (requireActivity() as SettingActivity).changeToolbarText(getString(R.string.title_question), false)

        binding.sendEmailView.setOnClickListener {
            val emailIntent: Intent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "message/rfc822"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
            startActivity(emailIntent)
        }
    }
}