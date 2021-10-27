package com.mangpo.bookclub.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mangpo.bookclub.databinding.ActivitySignInBinding
import com.mangpo.bookclub.view.book_profile.BookProfileInitActivity

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

        binding.signinCompleteTv.setOnClickListener {
            val intent: Intent = Intent(this, BookProfileInitActivity::class.java)
            startActivity(intent)
        }
    }
}