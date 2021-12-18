package com.mangpo.bookclub.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.mangpo.bookclub.databinding.ActivitySettingBinding
import com.mangpo.bookclub.util.AccountSharedPreference
import com.mangpo.bookclub.view.my_info.ResettingPasswordActivity
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    private val mainVm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SettingActivity", "onCreate")

        observe()

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ResettingPasswordActivity 화면으로 이동
        binding.resettingPasswordNextView.setOnClickListener {
            startActivity(Intent(this, ResettingPasswordActivity::class.java))
        }

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.logoutTv.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                mainVm.logout()
            }
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

    private fun observe() {
        mainVm.logoutCode.observe(this, Observer {
            AccountSharedPreference.clearUser(this)
            AccountSharedPreference.setJWT("")
            startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
        })
    }
}