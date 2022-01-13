package com.mangpo.bookclub.view.book_profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.mangpo.bookclub.databinding.ActivityEmailAuthenticationBinding
import com.mangpo.bookclub.util.AccountSharedPreference
import com.mangpo.bookclub.view.LoginActivity
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

//prevActivity: 이전 액티비티 -> 0이면 LoginActivity, 1이면 SignInActivity
class EmailAuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmailAuthenticationBinding
    private var prevActivity: Int? = null
    private val mainVm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observe()

        prevActivity = intent.getIntExtra("prevActivity", -1)   //이전 액티비티가 뭐였는지 알아오기
        val email = intent.getStringExtra("email")  //사용자가 입력한 이메일 가져오기

        if (prevActivity == 0)
            Toast.makeText(this, "이메일 인증이 안 된 계정입니다.\n이메일 인증을 완료해주세요.", Toast.LENGTH_LONG).show()

        binding.emailEt.setText(email)  //사용자가 입력한 이메일을 보여주기

        //뒤로가기 버튼을 누르면 LoginActivity 로 이동한다.
        binding.emailAuthToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.sendBtn.setOnClickListener {
            binding.sendBtn.visibility = View.INVISIBLE
            binding.resendBtn.visibility = View.VISIBLE

            sendEmail()
        }

        binding.resendBtn.setOnClickListener {
            sendEmail()
        }

        binding.emailAuthenticateCompleteTv.setOnClickListener {
            if (binding.codeEt.text.isBlank()) {
                Toast.makeText(
                    this@EmailAuthenticationActivity,
                    "인증코드를 입력해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.codeEt.requestFocus()
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    mainVm.sendCode(binding.codeEt.text.toString().toInt())
                }
            }
        }
    }

    //뒤로가기 버튼을 누르면 LoginActivity 로 이동한다.
    override fun onBackPressed() {
        AccountSharedPreference.clearJWT(this@EmailAuthenticationActivity)

        //이전 액티비티가 SplashActivity 또는 LoginActivity 이면 LoginActivity 로 이동하고,
        //SignInActivity 이면 SignInActivity 로 이동한다.
        if (prevActivity == 0) {
            startActivity(Intent(this@EmailAuthenticationActivity, LoginActivity::class.java))
        }

        super.onBackPressed()
    }

    //이메일 전송하기
    private fun sendEmail() {
        CoroutineScope(Dispatchers.Main).launch {
            mainVm.sendEmail()
            Toast.makeText(
                this@EmailAuthenticationActivity,
                "인증코드를 발송했습니다.\n인증코드를 받지 못하셨다면 재전송 버튼을 눌러주세요.",
                Toast.LENGTH_LONG
            ).show()
            binding.codeEt.requestFocus()
        }
    }

    private fun getUser() {
        CoroutineScope(Dispatchers.Main).launch {
            mainVm.getUser()
        }
    }

    private fun observe() {
        mainVm.sendCodeResult.observe(this, Observer {
            if (it == 0) {    //이메일 인증 실패
                binding.codeAlertTv.visibility = View.VISIBLE
            } else {    //이메일 인증 성공
                binding.codeAlertTv.visibility = View.INVISIBLE //인증코드 오류 메세지 숨기기
                getUser()
            }
        })

        mainVm.user.observe(this, Observer {
            val intent =
                Intent(this@EmailAuthenticationActivity, BookProfileInitActivity::class.java)
            intent.putExtra("user", Gson().toJson(it))
            startActivity(intent)
            finish()
        })
    }
}