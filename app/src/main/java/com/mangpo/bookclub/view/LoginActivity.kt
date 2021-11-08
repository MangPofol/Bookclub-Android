package com.mangpo.bookclub.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.*
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.mangpo.bookclub.databinding.ActivityLoginBinding
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.MainViewModel
import gun0912.tedimagepicker.util.ToastUtil.context
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val mainVm: MainViewModel by viewModel()
    private val loginEditJson: JsonObject = JsonObject()

    /*private val networkCallBack = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
        }

        override fun onLost(network: Network) {
            // 네트워크가 끊길 때 호출됩니다.
            Toast.makeText(baseContext, "와이파이나 데이터 접속이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observe()

        binding.loginSigninTv.setOnClickListener {  //회원가입 화면으로 이동
            if (checkNetwork()==null) {
                Toast.makeText(baseContext, "와이파이나 데이터 접속이 필요합니다.", Toast.LENGTH_SHORT).show()
            } else {
                val intent: Intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
        }

        binding.loginBtn.outlineProvider = null //로그인 버튼 그림자 제거

        binding.loginBtn.setOnClickListener {   //로그인 버튼 클릭 이벤트 리스너
            downKeyboard()  //키보드 내리기

            if (checkNetwork()==null)   //네트워크 연결 상태 확인
                Toast.makeText(baseContext, "와이파이나 데이터 접속이 필요합니다.", Toast.LENGTH_SHORT).show()
            else if (validationLogin()) {    //로그인 유효성 검사
                //로그인 입력값 JSON 객체로 저장
                loginEditJson.addProperty("email", binding.loginIdEt.text.toString())
                loginEditJson.addProperty("password", binding.loginPasswordEt.text.toString())

                //로그인 요청 api 전송
                CoroutineScope(Dispatchers.Main).launch {
                    mainVm.login(loginEditJson)
                }
            } else {
                Toast.makeText(baseContext, "아이디와 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //registerNetworkCallback()
    }

    override fun onStop() {
        super.onStop()
        //terminateNetworkCallback()
    }

    private fun goToMain() {
        val mPreferences = getSharedPreferences("emailPreferences", MODE_PRIVATE)
        val preferencesEditor: SharedPreferences.Editor = mPreferences.edit()
        preferencesEditor.putString("email", loginEditJson.get("email").asString)
        preferencesEditor.apply()

        val intent: Intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun validationLogin(): Boolean =
        !(binding.loginIdEt.text.isBlank() || binding.loginPasswordEt.text.isBlank())

    private fun observe() {
        mainVm.loginCode.observe(this, Observer {
            when (it) {
                200 -> {
                    goToMain()
                }
                -1 -> {
                    Toast.makeText(baseContext, "인터넷 연결이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    // 콜백을 등록하는 함수
   /* private fun registerNetworkCallback() {
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallBack)
    }*/

    // 콜백을 해제하는 함수
    /*private fun terminateNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallBack)
    }*/

    private fun checkNetwork(): Network? {
        val connectivityManager = getSystemService(ConnectivityManager::class.java)

        return connectivityManager.activeNetwork
    }

    private fun downKeyboard() {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.loginIdEt.windowToken, 0)
    }
}