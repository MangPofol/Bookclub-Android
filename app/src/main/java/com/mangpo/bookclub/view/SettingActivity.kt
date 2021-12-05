package com.mangpo.bookclub.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.mangpo.bookclub.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SettingActivity", "onCreate")

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIv.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("SettingActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("SettingActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("SettingActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("SettingActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SettingActivity", "onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("SettingActivity", "onRestart")
    }
}