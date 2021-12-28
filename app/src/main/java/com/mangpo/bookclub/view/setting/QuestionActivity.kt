package com.mangpo.bookclub.view.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityQuestionBinding

class QuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("InquiryAndRequestActivity", "onCreate")
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로가기 클릭 리스너
        binding.backIvView.setOnClickListener {
            finish()
        }

        //이메일 보내기 기능
        binding.sendEmailView.setOnClickListener {
            val emailIntent: Intent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "message/rfc822"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
            startActivity(emailIntent)
        }
    }
}