package com.mangpo.bookclub.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mangpo.bookclub.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signinAppbar.outlineProvider = null //Appbar Layout 그림자 제거

        binding.signinToolbar.setNavigationOnClickListener {    //뒤로가기 클릭 -> 액티비티 종료
            finish()
        }
    }
}