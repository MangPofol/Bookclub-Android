package com.mangpo.bookclub.view.my_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityResettingPasswordBinding
import com.mangpo.bookclub.util.AuthUtils
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResettingPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResettingPasswordBinding

    private val mainVm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResettingPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe()

        //뒤로가기 버튼 클릭 리스너
        binding.backIvView.setOnClickListener {
            finish()
        }

        //완료 버튼 클릭 리스너
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

        val oldPassword = AuthUtils.getPassword(this@ResettingPasswordActivity)
        if (oldPassword == binding.oldPasswordEt.text.toString()) {
            CoroutineScope(Dispatchers.Main).launch {
                mainVm.changePW(password)
            }
        } else
            Toast.makeText(
                this@ResettingPasswordActivity,
                "기존 비밀번호가 일치하지 않습니다.",
                Toast.LENGTH_SHORT
            ).show()
    }

    private fun observe() {
        mainVm.updateUserCode.observe(this, Observer {
            if (it == 204) {
                Toast.makeText(this, getString(R.string.msg_password_resetting), Toast.LENGTH_SHORT)
                    .show()

                AuthUtils.setPassword(
                    this@ResettingPasswordActivity,
                    binding.newPasswordEt.text.toString()
                )

                finish()
            } else
                Toast.makeText(this, getString(R.string.err_change), Toast.LENGTH_SHORT)
                    .show()
        })
    }
}