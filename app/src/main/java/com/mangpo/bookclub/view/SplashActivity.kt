package com.mangpo.bookclub.view

import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivitySplashBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.util.AuthUtils
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

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe()

        //이미지뷰에 gif 파일 넣기
        Glide.with(applicationContext).load(R.drawable.loading).into(binding.logoIv)

        if (checkNetwork() == null) { //네트워크 연결 상태 확인
            Toast.makeText(baseContext, "와이파이나 데이터 접속이 필요합니다.", Toast.LENGTH_SHORT).show()
            finish()
        } else
            autoLogin()
    }

    private fun autoLogin() {
        val email = AuthUtils.getEmail(this@SplashActivity)
        val password = AuthUtils.getPassword(this@SplashActivity)

        if (email.isBlank() || password.isBlank())
            goLoginActivity()
        else
            login(UserModel(email = email, password = password))

    }

    private fun goLoginActivity() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000L)
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun login(user: UserModel) {
        CoroutineScope(Dispatchers.IO).launch {
            mainVm.login(user)
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
        mainVm.loginCode.observe(this, Observer {
            if (it == 200) {
                role = checkRole(AuthUtils.getJWT(this@SplashActivity))

                when (role) {
                    "ROLE_NEED_EMAIL" -> {  //이메일 인증이 안된 유저
                        //getUser 를 통해 이메일 정보를 받아와서 EmailAuthenticationActivity 화면으로 이동한다.
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(2000L)
                            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        }
                    }
                    "ROLE_USER" -> {    //이메일 인증이 된 유저
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(2000L)
                            mainVm.getUser()
                        }
                    }
                }
            } else {
                Toast.makeText(
                    this@SplashActivity,
                    getString(R.string.err_login_fail_go_login),
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
        })

        mainVm.user.observe(this, Observer {
            if (!validateUser(it)) //role==ROLE_USER && user.nickname==null(인증은 됐는데 필수 데이터가 없음.)
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            else    //정상적인 계정
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))

            finish()
        })
    }
}