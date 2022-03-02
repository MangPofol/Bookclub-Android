package com.mangpo.ourpage.view.setting

import android.content.Intent
import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.FragmentQuestionBinding
import com.mangpo.ourpage.view.BaseFragment

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