package com.mangpo.bookclub.view

import android.content.Context
import android.content.Intent
import android.net.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityLoginBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.util.JWTUtils
import com.mangpo.bookclub.util.BackStackManager
import com.mangpo.bookclub.view.book_profile.BookProfileInitActivity
import com.mangpo.bookclub.view.book_profile.EmailAuthenticationActivity
import com.mangpo.bookclub.view.book_profile.SignInActivity
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.view.my_info.PasswordReissueActivity
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.*
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val mainVm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LoginActivity", "onCreate")

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe()

        binding.loginSigninClickView.setOnClickListener {  //회원가입 화면으로 이동
            if (checkNetwork() == null) {
                Toast.makeText(baseContext, "와이파이나 데이터 접속이 필요합니다.", Toast.LENGTH_SHORT).show()
            } else {
                val intent: Intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
        }

        //비밀번호 찾기 클릭 리스너 -> 비밀번호 재발급 화면 이동
        binding.loginFindPasswordClickView.setOnClickListener {
            startActivity(Intent(this@LoginActivity, PasswordReissueActivity::class.java))
        }

        binding.loginBtn.outlineProvider = null //로그인 버튼 그림자 제거

        binding.loginBtn.setOnClickListener {   //로그인 버튼 클릭 이벤트 리스너
            downKeyboard()  //키보드 내리기

            when {
                checkNetwork() == null   //네트워크 연결 상태 확인
                -> Toast.makeText(baseContext, "와이파이나 데이터 접속이 필요합니다.", Toast.LENGTH_SHORT).show()
                validationLogin() -> {    //로그인 유효성 검사
                    val user: UserModel = UserModel(
                        email = binding.loginIdEt.text.toString(),
                        password = binding.loginPasswordEt.text.toString()
                    )

                    //로그인 요청 api 전송
                    CoroutineScope(Dispatchers.Main).launch {
                        val token = mainVm.login(user)

                        if (token != null) {
                            JWTUtils.setJWT(this@LoginActivity, token)

                            when (val role = checkRole(token)) {
                                "ROLE_NEED_EMAIL" -> {
                                    val intent = Intent(
                                        this@LoginActivity,
                                        EmailAuthenticationActivity::class.java
                                    )
                                    intent.putExtra("email", binding.loginIdEt.text.toString())
                                    intent.putExtra("prevActivity", 0)
                                    startActivity(intent)
                                    finish()
                                }
                                "ROLE_USER" -> {
                                    mainVm.getUser()
                                }
                            }
                        } else {
                            binding.loginMsgTv.visibility = View.VISIBLE
                            binding.loginMsgTv.text = getString(R.string.err_wrong_user)
                        }
                    }
                }
                else -> {
                    binding.loginMsgTv.visibility = View.VISIBLE
                    binding.loginMsgTv.text = getString(R.string.err_input_id_pw)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("LoginActivity", "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LoginActivity", "onStop")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun validationLogin(): Boolean =
        !(binding.loginIdEt.text.isBlank() || binding.loginPasswordEt.text.isBlank())

    private fun checkNetwork(): Network? {
        val connectivityManager = getSystemService(ConnectivityManager::class.java)

        return connectivityManager.activeNetwork
    }

    private fun downKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.loginIdEt.windowToken, 0)
    }

    //JWT Token Decode -> Find User Role
    private fun checkRole(token: String): String {
        val splitToken = token.split(".")
        val payloadByte = Base64.getUrlDecoder().decode(splitToken[1])
        val payload = payloadByte.toString(Charsets.UTF_8)

        return JSONObject(payload).getString("auth")
    }

    private fun validateUser(user: UserModel): Boolean =
        !(user.nickname == null || user.sex == null || user.birthdate == null || user.profileImgLocation == "")

    private fun observe() {
        mainVm.user.observe(this, Observer {
            BackStackManager.clear()

            if (validateUser(it))   //정상 계정인 경우
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            else {   //role==ROLE_USER && user.nickname==null(인증은 됐는데 필수 데이터가 없음.)
                val intent = Intent(this@LoginActivity, BookProfileInitActivity::class.java)
                intent.putExtra("user", Gson().toJson(it))
                startActivity(intent)
            }

            finish()
        })
    }
}