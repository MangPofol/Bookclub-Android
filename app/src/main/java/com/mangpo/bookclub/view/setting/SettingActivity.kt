package com.mangpo.bookclub.view.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivitySettingBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.util.AccountSharedPreference
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
        Log.d("SettingActivity", "onCreate")

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe()

        getUser()

        //ResettingPasswordActivity 화면으로 이동
        binding.resettingPasswordNextView.setOnClickListener {
            val userJson = Gson().toJson(user)
            val intent = Intent(this, ResettingPasswordActivity::class.java)
            intent.putExtra("user", userJson)
            startActivity(intent)
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
            quitMembership()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("SettingActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("SettingActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("SettingActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("SettingActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SettingActivity", "onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("SettingActivity", "onRestart")
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

    //계정 탈퇴 함수
    private fun quitMembership() {
        CoroutineScope(Dispatchers.IO).launch {
            mainVm.quitMembership(user.userId!!)
        }
    }

    //로그아웃 or 계정 탈퇴 시 사용하는 함수 -> 사용자 관련 sharedPreferences clear, JWT 빈값으로
    private fun clearUser() {
        AccountSharedPreference.clearJWT(this)
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

        mainVm.quitMembershipCode.observe(this, Observer {
            if (it == 204) {
                Toast.makeText(
                    this@SettingActivity,
                    getString(R.string.msg_quit_membership),
                    Toast.LENGTH_SHORT
                ).show()
                clearUser()
                val intent = Intent(this@SettingActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@SettingActivity,
                    getString(R.string.err_quit_membership),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}