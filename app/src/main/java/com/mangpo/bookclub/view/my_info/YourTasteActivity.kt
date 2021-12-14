package com.mangpo.bookclub.view.my_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mangpo.bookclub.databinding.ActivityYourTasteBinding

class YourTasteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityYourTasteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYourTasteBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.backIvView.setOnClickListener {
            finish()
        }
    }
}