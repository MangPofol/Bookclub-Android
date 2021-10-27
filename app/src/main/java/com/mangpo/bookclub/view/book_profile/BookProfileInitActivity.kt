package com.mangpo.bookclub.view.book_profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mangpo.bookclub.databinding.ActivityBookProfileInitBinding

class BookProfileInitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookProfileInitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookProfileInitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIv.setOnClickListener { //뒤로가기 클릭 -> 액티비티 종료
            finish()
        }

        binding.nextBtn.outlineProvider = null  //다음 버튼 그림자 제거

        supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, BookProfileDescFragment()).commit()
    }
}