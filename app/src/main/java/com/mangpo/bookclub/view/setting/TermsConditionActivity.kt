package com.mangpo.bookclub.view.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mangpo.bookclub.databinding.ActivityTermsConditionBinding

class TermsConditionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermsConditionBinding
    private lateinit var termsBottomSheetFragment: TermsBottomSheetFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TermsConditionActivity", "onCreate")
        binding = ActivityTermsConditionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로가기 이벤트
        binding.backIvView.setOnClickListener {
            finish()
        }

        //각 이용약관의 본문이 적혀있는 BottomSheetDialogFragment 띄우기
        binding.termsOfServiceNextView.setOnClickListener {
            termsBottomSheetFragment = TermsBottomSheetFragment.newInstance("service")
            termsBottomSheetFragment.show(supportFragmentManager, termsBottomSheetFragment.tag)
        }
        binding.privacyPolicyNextView.setOnClickListener {
            termsBottomSheetFragment = TermsBottomSheetFragment.newInstance("privacy")
            termsBottomSheetFragment.show(supportFragmentManager, termsBottomSheetFragment.tag)
        }
        binding.receiveMarketingInformationNextView.setOnClickListener {
            termsBottomSheetFragment = TermsBottomSheetFragment.newInstance("marketing")
            termsBottomSheetFragment.show(supportFragmentManager, termsBottomSheetFragment.tag)
        }
    }
}