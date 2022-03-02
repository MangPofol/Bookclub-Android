package com.mangpo.ourpage.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.ActivitySplashBinding
import com.mangpo.ourpage.model.entities.LoginUser
import com.mangpo.ourpage.utils.AuthUtils
import com.mangpo.ourpage.utils.isNetworkAvailable
import com.mangpo.ourpage.view.auth.LoginActivity
import com.mangpo.ourpage.view.auth.signin.SignInActivity
import com.mangpo.ourpage.view.main.MainActivity
import com.mangpo.ourpage.viewmodel.AuthViewModel
import com.mangpo.ourpage.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    private val authVm: AuthViewModel by viewModels<AuthViewModel>()
    private val userVm: UserViewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initAfterBinding() {
        //이미지뷰에 gif 파일 넣기
        Glide.with(this).load(R.raw.loading).into(binding.splashLogoIv)

        if (!isNetworkAvailable(this))  //데이터나 와이파이 연결이 안 돼 있을 때
            showSnackBar(getString(R.string.error_check_network))
        else
            autoLogin()

        observe()
    }

    //자동로그인 함수
    private fun autoLogin() {
        //암호화된 SharedPreferences 로부터 저장된 email 과 password 가 있는지 확인한다.
        val email = AuthUtils.getEmail()
        val password = AuthUtils.getPassword()

        if (email.isBlank() || password.isBlank())  //없으면 로그인 액티비티로 이동
            goLoginActivity()
        else  //있으면 로그인 하기
            authVm.login(LoginUser(AuthUtils.getEmail(), AuthUtils.getPassword()))
    }

    private fun goLoginActivity() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000L)
            startActivityWithClear(LoginActivity::class.java)
        }
    }

    private fun goSignInActivity(mode: String) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000L)

            val intent: Intent = Intent(this@SplashActivity, SignInActivity::class.java)
            intent.putExtra("mode", mode)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun hasProfile() {
        val user = userVm.getUser()!!

        if (user.nickname==null)
            goSignInActivity("ROLE_NEED_PROFILE")
        else
            startActivityWithClear(MainActivity::class.java)
    }

    //역할 확인
    private fun checkRole(token: String): String {
        val splitToken = token.split(".")
        val payloadByte = Base64.getUrlDecoder().decode(splitToken[1])
        val payload = payloadByte.toString(Charsets.UTF_8)

        return JSONObject(payload).getString("auth")
    }

    private fun observe() {
        authVm.loginSuccess.observe(this, Observer {
            if (it) {
                when (checkRole(AuthUtils.getJWT())) {
                    "ROLE_NEED_EMAIL" -> goSignInActivity("ROLE_NEED_EMAIL")  //이메일 인증이 안된 유저
                    "ROLE_USER" -> userVm.getUserInfo()    //이메일 인증이 된 유저
                }
            } else
                goLoginActivity()
        })

        userVm.getUserCode.observe(this, Observer {
            val code = it.getContentIfNotHandled()
            if (code!=null) {
                when (code) {
                    200 -> hasProfile()
                    else -> showSnackBar(getString(R.string.error_api))
                }
            }
        })
    }
}