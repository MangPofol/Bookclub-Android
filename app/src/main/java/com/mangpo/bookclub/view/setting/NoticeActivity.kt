package com.mangpo.bookclub.view.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mangpo.bookclub.databinding.ActivityNoticeBinding

class NoticeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("NoticeActivity", "onCreate")
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로가기 클릭 리스너
        binding.backIvView.setOnClickListener {
            finish()
        }
    }
}