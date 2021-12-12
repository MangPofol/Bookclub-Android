package com.mangpo.bookclub.view.my_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mangpo.bookclub.databinding.ActivityMyInfoBinding

class MyInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}