package com.example.bookclub.view.bookclub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookclub.databinding.ActivityCreateClubBinding

class CreateClubActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateClubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateClubBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}