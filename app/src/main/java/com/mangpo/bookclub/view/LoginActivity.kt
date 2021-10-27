package com.mangpo.bookclub.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSigninTv.setOnClickListener {  //회원가입 화면으로 이동
            val intent: Intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.outlineProvider = null //로그인 버튼 그림자 제거
    }
}