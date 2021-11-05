package com.mangpo.bookclub.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mangpo.bookclub.databinding.ActivitySignInBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.view.book_profile.BookProfileInitActivity
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInActivity : AppCompatActivity(), TextWatcher {
    private lateinit var binding: ActivitySignInBinding

    private val mainVm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observe()   //mainViewModel livedate observe 함수

        binding.signinAppbar.outlineProvider = null //Appbar Layout 그림자 제거

        //EditText TextWatcher 리스너 등록
        binding.signinIdEt.addTextChangedListener(this)
        binding.signinPasswordEt.addTextChangedListener(this)
        binding.signinPasswordConfirmEt.addTextChangedListener(this)

        binding.signinToolbar.setNavigationOnClickListener {    //뒤로가기 클릭 -> 액티비티 종료
            finish()
        }

        binding.signinCompleteTv.setOnClickListener {   //회원가입 완료 버튼 클릭 리스너
            if (checkEdit()) {
                val mPreferences = getSharedPreferences("signInPreferences", MODE_PRIVATE)
                val preferencesEditor: SharedPreferences.Editor = mPreferences.edit()
                val userJson: JsonObject = JsonObject()

                userJson.addProperty("email", binding.signinIdEt.text.toString())
                userJson.addProperty("password", binding.signinPasswordEt.text.toString())

                preferencesEditor.putString("newUser", Gson().toJson(userJson))
                preferencesEditor.apply()

                val intent: Intent = Intent(this, BookProfileInitActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "아이디와 비밀번호를 모두 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (binding.signinIdEt.hasFocus()) {    //아이디 중복확인
            val emailJson: JsonObject = JsonObject()
            emailJson.addProperty("email", s.toString())
            validateEmail(emailJson)
        } else if (binding.signinPasswordEt.hasFocus()) { //비밀번호 6~12자
            if (s?.length in 6..12)
                binding.signinPasswordAlertTv.visibility = View.INVISIBLE
            else
                binding.signinPasswordAlertTv.visibility = View.VISIBLE
        } else {    //비밀번호 6~12자 & 비밀번호, 비밀번호 확인 일치 여부
            if (binding.signinPasswordEt.text.toString() == s.toString() && s?.length in 6..12) {
                binding.signinPasswordAlertTv.visibility = View.INVISIBLE
            } else {
                binding.signinPasswordAlertTv.visibility = View.VISIBLE
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    private fun validateEmail(email: JsonObject) {
        CoroutineScope(Dispatchers.Main).launch {
            mainVm.validateEmail(email)
        }
    }

    private fun observe() {
        mainVm.emailAlertVisibility.observe(this, Observer {
            binding.signinIdAlertTv.visibility = it
        })
    }

    private fun checkEdit(): Boolean =
        !(binding.signinIdEt.text.isBlank() || binding.signinIdAlertTv.visibility == View.VISIBLE ||
                binding.signinPasswordEt.text.isBlank() || binding.signinPasswordConfirmEt.text.isBlank() ||
                binding.signinPasswordAlertTv.visibility == View.VISIBLE)

}