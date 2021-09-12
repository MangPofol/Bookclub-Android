package com.example.bookclub.view.bookclub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.bookclub.databinding.ActivityCreateClubBinding

class CreateClubActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateClubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateClubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //시스템 툴바 보이도록
        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
    }
}