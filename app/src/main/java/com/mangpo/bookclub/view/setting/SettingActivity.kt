package com.mangpo.bookclub.view.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.mangpo.bookclub.databinding.ActivitySettingBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.util.AuthUtils
import com.mangpo.bookclub.view.LoginActivity
import com.mangpo.bookclub.view.my_info.ResettingPasswordActivity
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var user: UserModel

    private val mainVm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe()

        getUser()

        //ResettingPasswordActivity 화면으로 이동
        binding.resettingPasswordNextView.setOnClickListener {
            startActivity(Intent(this, ResettingPasswordActivity::class.java))
        }

        //NoticeActivity 화면으로 이동
        binding.noticeNextView.setOnClickListener {
            startActivity(Intent(this@SettingActivity, NoticeActivity::class.java))
        }

        //QuestionActivity 화면으로 이동
        binding.questionNextView.setOnClickListener {
            startActivity(Intent(this@SettingActivity, QuestionActivity::class.java))
        }

        //TermsConditionActivity 화면으로 이동
        binding.termsConditionsNextView.setOnClickListener {
            startActivity(Intent(this@SettingActivity, TermsConditionActivity::class.java))
        }

        //OpenSourceActivity 화면으로 이동
        binding.openSourceLicenseNextView.setOnClickListener {
            startActivity(Intent(this@SettingActivity, OpenSourceActivity::class.java))
        }

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.logoutTv.setOnClickListener {
            logout()
        }

        //계정 탈퇴하기 클릭 리스너
        binding.quitMembershipTv.setOnClickListener {
            val intent = Intent(this@SettingActivity, WithdrawalActivity::class.java)
            intent.putExtra("userId", user.userId)
            startActivity(intent)
        }
    }

    private fun getUser() {
        CoroutineScope(Dispatchers.IO).launch {
            mainVm.getUser()
        }
    }

    private fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            mainVm.logout()
        }
    }

    //로그아웃 or 계정 탈퇴 시 사용하는 함수 -> 사용자 관련 sharedPreferences clear, JWT 빈값으로
    private fun clearUser() {
        AuthUtils.clear(this)
    }

    private fun observe() {
        mainVm.user.observe(this, Observer {
            user = it
        })

        mainVm.logoutCode.observe(this, Observer {
            clearUser()
            val intent = Intent(this@SettingActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        })

        mainVm.updateUserCode.observe(this, Observer {
            if (it == 204)
                logout()
        })
    }
}