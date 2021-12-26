package com.mangpo.bookclub.view.my_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mangpo.bookclub.databinding.ActivityResettingPasswordBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResettingPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResettingPasswordBinding
    private lateinit var user: UserModel

    private val mainVm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ResettingPasswordActivity", "onCreate")

        binding = ActivityResettingPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe()

        user = Gson().fromJson(intent.getStringExtra("user"), UserModel::class.java)

        binding.completeTv.setOnClickListener {
            validate()
        }
    }

    //비밀번호 변경 요청 전 유효성 검사
    private fun validate() {
        if (binding.oldPasswordEt.text.isBlank()) {
            Toast.makeText(this, "기존 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
        } else if (binding.newPasswordEt.text.isBlank() || binding.reenterNewPasswordEt.text.isBlank()) {
            Toast.makeText(this, "새로운 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
        } else if (binding.newPasswordEt.text.length < 6) {
            binding.passwordConditionTv.visibility = View.VISIBLE
        } else if (binding.newPasswordEt.text.toString() != binding.reenterNewPasswordEt.text.toString()) {
            Toast.makeText(this, "새로운 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
        } else {
            updatePassword()
        }
    }

    //비밀번호 변경하기
    private fun updatePassword() {
        val password = binding.newPasswordEt.text.toString()
        val loginJson = JsonObject()
        loginJson.addProperty("email", user.email)
        loginJson.addProperty("password", binding.oldPasswordEt.text.toString())

        CoroutineScope(Dispatchers.Main).launch {
            val token = mainVm.login(loginJson) //기존 비밀번호가 일치하는지 확인

            if (token==null) {
                Toast.makeText(this@ResettingPasswordActivity, "기존 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else {
                mainVm.changePW(password)
            }
        }
    }

    private fun observe() {
        mainVm.updateUserCode.observe(this, Observer {
            if (it == 204) {
                Toast.makeText(this, "비밀번호 변경 완료.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "비밀번호 변경 중 오류 발생!\n다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}