package com.mangpo.bookclub.view.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityWithdrawalBinding
import com.mangpo.bookclub.util.JWTUtils
import com.mangpo.bookclub.view.LoginActivity
import com.mangpo.bookclub.viewmodel.MainViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WithdrawalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWithdrawalBinding

    private var userId: Long? = null

    private val postVm: PostViewModel by viewModel()
    private val mainVm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWithdrawalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra("userId", -1)

        observe()
        setMyClickListener()
        getTotalPostCnt()
    }

    private fun getTotalPostCnt() {
        CoroutineScope(Dispatchers.Main).launch {
            postVm.getTotalPostCnt()
        }
    }

    private fun setMyClickListener() {
        binding.backIv.setOnClickListener {
            finish()
        }

        binding.withdrawalBtn.setOnClickListener {
            quitMembership()
        }
    }

    //계정 탈퇴 함수
    private fun quitMembership() {
        CoroutineScope(Dispatchers.IO).launch {
            mainVm.quitMembership(userId!!)
        }
    }

    //로그아웃 or 계정 탈퇴 시 사용하는 함수 -> 사용자 관련 sharedPreferences clear, JWT 빈값으로
    private fun clearUser() {
        JWTUtils.clearJWT(this)
    }

    private fun observe() {
        postVm.totalCnt.observe(this, Observer {
            binding.titleTv.text = "Our Page를 떠나면\n${it}개 기록들이 사라져요."
        })

        mainVm.quitMembershipCode.observe(this, Observer {
            if (it == 204) {
                Toast.makeText(
                    this@WithdrawalActivity,
                    getString(R.string.msg_quit_membership),
                    Toast.LENGTH_SHORT
                ).show()
                clearUser()
                val intent = Intent(this@WithdrawalActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@WithdrawalActivity,
                    getString(R.string.err_quit_membership),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}