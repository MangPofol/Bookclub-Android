package com.mangpo.bookclub.view.my_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityResettingPasswordBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.util.AccountSharedPreference
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

        getUser()

        binding.completeTv.setOnClickListener {
            validate()
        }
    }

    private fun getUser() {
        CoroutineScope(Dispatchers.IO).launch {
            mainVm.getUser()
        }
    }

    private fun validate() {
        binding.passwordConditionTv.setTextColor(ContextCompat.getColor(this, R.color.grey12))

        if (binding.oldPasswordEt.text.isBlank()) {
            Toast.makeText(this, "기존 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
        } else if (binding.newPasswordEt.text.isBlank() || binding.reenterNewPasswordEt.text.isBlank()) {
            Toast.makeText(this, "새로운 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
        } else if (binding.oldPasswordEt.text.toString() != AccountSharedPreference.getUserPass(this)) {
            Toast.makeText(this, "기존 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
        } else if (binding.newPasswordEt.text.length < 6) {
            binding.passwordConditionTv.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.light_red
                )
            )
        } else if (binding.newPasswordEt.text.toString() != binding.reenterNewPasswordEt.text.toString()) {
            Toast.makeText(this, "새로운 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
        } else {
            updatePassword()
        }
    }

    private fun updatePassword() {
        user.password = binding.newPasswordEt.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            mainVm.updateUser(user)
        }
    }

    private fun observe() {
        mainVm.user.observe(this, Observer {
            user = it
        })

        mainVm.updateUserCode.observe(this, Observer {
            if (it == 204) {
                AccountSharedPreference.setUserPass(this, user.password!!)
                Toast.makeText(this, "비밀번호 변경 완료.", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }
}