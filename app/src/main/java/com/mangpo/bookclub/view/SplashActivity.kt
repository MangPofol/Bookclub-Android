package com.mangpo.bookclub.view

import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivitySplashBinding
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private val mainVm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SplashActivity", "onCreate")

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe()

        Glide.with(applicationContext).load(R.drawable.loading)
            .into(binding.loadingIv)   //이미지뷰에 gif 파일 넣기

        if (checkNetwork() == null) { //네트워크 연결 상태 확인
            Toast.makeText(baseContext, "와이파이나 데이터 접속이 필요합니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        CoroutineScope(Dispatchers.IO).launch {
            mainVm.getUser()
        }
    }

    private fun observe() {
        mainVm.user.observe(this, Observer {
            //로그인 기록이 있으면 MainActivity 로 이동하고, 없으면 2초 후 LoginActivity 화면으로 이동한다.
            if (it.userId != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(2000L)
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    val userStr = Gson().toJson(it)
                    intent.putExtra("user", userStr)
                    startActivity(intent)
                    finish()
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(2000L)
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()
                }
            }
        })
    }

    private fun checkNetwork(): Network? {
        val connectivityManager = getSystemService(ConnectivityManager::class.java)

        return connectivityManager.activeNetwork
    }

}