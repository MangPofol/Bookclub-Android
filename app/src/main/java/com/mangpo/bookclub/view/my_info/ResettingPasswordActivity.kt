package com.mangpo.bookclub.view.my_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mangpo.bookclub.databinding.ActivityResettingPasswordBinding

class ResettingPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResettingPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResettingPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}