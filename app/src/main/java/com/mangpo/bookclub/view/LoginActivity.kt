package com.mangpo.bookclub.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.mangpo.bookclub.databinding.ActivityLoginBinding
import com.mangpo.bookclub.repository.UserRepository
import com.mangpo.bookclub.service.ApiClient
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.MainViewModel
import com.mangpo.bookclub.viewmodel.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()

        binding.loginSigninTv.setOnClickListener {  //회원가입 화면으로 이동
            val intent: Intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.outlineProvider = null //로그인 버튼 그림자 제거

        binding.loginBtn.setOnClickListener {   //로그인 버튼 클릭 이벤트 리스너
            //키보드 내리기
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.loginIdEt.windowToken, 0)

            if (validationLogin()) {    //로그인 유효성 검사
                //로그인 입력값 JSON 객체로 저장
                val loginEditJson: JsonObject = JsonObject()
                loginEditJson.addProperty("email", binding.loginIdEt.text.toString())
                loginEditJson.addProperty("password", binding.loginPasswordEt.text.toString())

                //로그인 요청 api 전송
                CoroutineScope(Dispatchers.IO).launch {
                    val result =
                        withContext(Dispatchers.Default) {
                            viewModel.login(loginEditJson)
                        }

                    if (result==200) {
                        goToMain()
                    } else {
                        runOnUiThread {
                            Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(baseContext, "아이디와 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMain() {
        val intent: Intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun initViewModel() {
        viewModelFactory = MainViewModelFactory(UserRepository(ApiClient.userService))
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private fun validationLogin(): Boolean =
        !(binding.loginIdEt.text.isBlank() || binding.loginPasswordEt.text.isBlank())
}