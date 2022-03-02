package com.mangpo.ourpage.view.auth

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.ActivityLoginBinding
import com.mangpo.ourpage.model.entities.LoginUser
import com.mangpo.ourpage.utils.AuthUtils
import com.mangpo.ourpage.utils.isNetworkAvailable
import com.mangpo.ourpage.view.BaseActivity
import com.mangpo.ourpage.view.main.MainActivity
import com.mangpo.ourpage.view.auth.signin.SignInActivity
import com.mangpo.ourpage.viewmodel.AuthViewModel
import com.mangpo.ourpage.viewmodel.UserViewModel
import org.json.JSONObject
import java.util.*

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {
    private val authVm: AuthViewModel by viewModels<AuthViewModel>()
    private val userVm: UserViewModel by viewModels<UserViewModel>()

    override fun initAfterBinding() {
        setClickListener()
        observe()
    }
    
    private fun setClickListener() {
        binding.loginBtn.setOnClickListener {
            if (!isNetworkAvailable(this)) {
                binding.loginMsgTv.visibility = View.INVISIBLE
                showSnackBar(getString(R.string.error_check_network))
            }
            else if (!validation()) {
                binding.loginMsgTv.text = getString(R.string.error_input_email_password)
                binding.loginMsgTv.visibility = View.VISIBLE
            } else {
                hideKeyboard(binding.root)
                binding.loginMsgTv.visibility = View.INVISIBLE
                val user: LoginUser = LoginUser(binding.loginIdEt.text.toString(), binding.loginPasswordEt.text.toString())
                authVm.login(user)
                showLoadingDialog()
            }
        }

        binding.loginSigninTv.setOnClickListener {  //회원가입 클릭 리스너 -> SignInActivity 로 이동
            startNextActivity(SignInActivity::class.java)
        }

        binding.loginFindPasswordTv.setOnClickListener {    //비밀번호 찾기 클릭 리스너
            startNextActivity(PasswordReissueActivity::class.java)
        }
    }

    private fun validation(): Boolean = !(binding.loginIdEt.text.isBlank() || binding.loginPasswordEt.text.isBlank())

    //역할 확인
    private fun checkRole(token: String): String {
        val splitToken = token.split(".")
        val payloadByte = Base64.getUrlDecoder().decode(splitToken[1])
        val payload = payloadByte.toString(Charsets.UTF_8)

        return JSONObject(payload).getString("auth")
    }

    private fun goSignInActivity(mode: String) {
        val intent: Intent = Intent(this@LoginActivity, SignInActivity::class.java)
        intent.putExtra("mode", mode)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun hasProfile() {
        val user = userVm.getUser()!!

        if (user.nickname==null)
            goSignInActivity("ROLE_NEED_PROFILE")
        else
            startActivityWithClear(MainActivity::class.java)
    }

    private fun observe() {
        authVm.loginSuccess.observe(this, Observer {
            dismissLoadingDialog()

            if (it) {
                when (checkRole(AuthUtils.getJWT())) {
                    "ROLE_NEED_EMAIL" -> goSignInActivity("ROLE_NEED_EMAIL")  //이메일 인증이 안된 유저
                    "ROLE_USER" -> userVm.getUserInfo()    //이메일 인증이 된 유저
                }
            }
            else {
                binding.loginMsgTv.text = getString(R.string.error_check_email_password)
                binding.loginMsgTv.visibility = View.VISIBLE
            }
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