package com.mangpo.bookclub.view.book_profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityBookProfileInitBinding
import gun0912.tedimagepicker.util.ToastUtil.context

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

        //frmame layout의 초기 화면 설정
        supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, BookProfileDescFragment()).commit()

        binding.nextBtn.setOnClickListener {
            when (supportFragmentManager.fragments[0].javaClass) {
                BookProfileDescFragment::class.java -> {
                    supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, SetNicknameFragment()).commit()
                }
            }
        }
    }

    fun unEnableNextBtn() {
        binding.nextBtn.isEnabled = false
        binding.nextBtn.setTextColor(ContextCompat.getColor(context, R.color.grey12))
        binding.nextBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.grey13))
    }

    fun enableNextBtn() {
        binding.nextBtn.isEnabled = true
        binding.nextBtn.setTextColor(ContextCompat.getColor(context, R.color.white))
        binding.nextBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red))
    }
}