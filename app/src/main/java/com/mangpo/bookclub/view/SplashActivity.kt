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
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivitySplashBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.util.JWTUtils
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var role: String

    private val mainVm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SplashActivity", "onCreate")

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe()

        Glide.with(applicationContext).load(R.drawable.loading)
            .into(binding.logoIv)   //이미지뷰에 gif 파일 넣기

        if (checkNetwork() == null) { //네트워크 연결 상태 확인
            Toast.makeText(baseContext, "와이파이나 데이터 접속이 필요합니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        val token = JWTUtils.getJWT(this)

        if (token == "") {  //로그인 안 됐을 때
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000L)
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        } else {    //로그인 돼 있을 때
            role = checkRole(token)

            when (role) {
                "ROLE_NEED_EMAIL" -> {  //이메일 인증이 안된 유저
                    //getUser 를 통해 이메일 정보를 받아와서 EmailAuthenticationActivity 화면으로 이동한다.
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000L)
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    }
                }
                "ROLE_USER" -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000L)
                        mainVm.getUser()
                    }
                }
            }
        }
    }

    //JWT Token Decode -> Find User Role
    private fun checkRole(token: String): String {
        val splitToken = token.split(".")
        val payloadByte = Base64.getUrlDecoder().decode(splitToken[1])
        val payload = payloadByte.toString(Charsets.UTF_8)

        return JSONObject(payload).getString("auth")
    }

    private fun checkNetwork(): Network? {
        val connectivityManager = getSystemService(ConnectivityManager::class.java)

        return connectivityManager.activeNetwork
    }

    private fun validateUser(user: UserModel): Boolean =
        !(user.nickname == null || user.sex == null || user.birthdate == null || user.profileImgLocation == "")

    private fun observe() {
        mainVm.user.observe(this, Observer {
            if (!validateUser(it)) {  //role==ROLE_USER && user.nickname==null(인증은 됐는데 필수 데이터가 없음.)
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            } else {    //정상적인 계정
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }

            finish()
        })
    }
}