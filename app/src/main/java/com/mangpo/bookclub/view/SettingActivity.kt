package com.mangpo.bookclub.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.mangpo.bookclub.databinding.ActivitySettingBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.util.AccountSharedPreference
import com.mangpo.bookclub.view.my_info.ResettingPasswordActivity
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var user: UserModel

    private val mainVm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SettingActivity", "onCreate")

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe()

        getUser()

        //ResettingPasswordActivity 화면으로 이동
        binding.resettingPasswordNextView.setOnClickListener {
            startActivity(Intent(this, ResettingPasswordActivity::class.java))
        }

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.logoutTv.setOnClickListener {
            logout()
        }

        binding.quitMembershipTv.setOnClickListener {
            quitMembership()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("SettingActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("SettingActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("SettingActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("SettingActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SettingActivity", "onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("SettingActivity", "onRestart")
    }

    private fun getUser() {
        CoroutineScope(Dispatchers.IO).launch {
            mainVm.getUser()
        }
    }

    private fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            mainVm.logout()
        }
    }

    private fun quitMembership() {
        CoroutineScope(Dispatchers.IO).launch {
            user.password = AccountSharedPreference.getUserPass(this@SettingActivity)
            user.isDormant = true
            mainVm.updateUser(user)
        }
    }

    private fun observe() {
        mainVm.user.observe(this, Observer {
            user = it
        })

        mainVm.logoutCode.observe(this, Observer {
            AccountSharedPreference.clearUser(this)
            AccountSharedPreference.setJWT("")
            startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
        })

        mainVm.updateUserCode.observe(this, Observer {
            Log.d("MainViewModel", "user Observe $user")
            Log.d("MainViewModel", "updateUserCode Observe $it")
            if (it == 204)
                logout()
        })
    }
}