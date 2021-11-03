package com.mangpo.bookclub.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.mangpo.bookclub.databinding.ActivitySignInBinding
import com.mangpo.bookclub.repository.UserRepository
import com.mangpo.bookclub.service.ApiClient
import com.mangpo.bookclub.view.book_profile.BookProfileInitActivity
import com.mangpo.bookclub.viewmodel.MainViewModel
import com.mangpo.bookclub.viewmodel.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInActivity : AppCompatActivity(), TextWatcher {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()

        binding.signinAppbar.outlineProvider = null //Appbar Layout 그림자 제거

        binding.signinIdEt.addTextChangedListener(this)

        binding.signinToolbar.setNavigationOnClickListener {    //뒤로가기 클릭 -> 액티비티 종료
            finish()
        }

        binding.signinCompleteTv.setOnClickListener {
            val intent: Intent = Intent(this, BookProfileInitActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViewModel() {
        viewModelFactory = MainViewModelFactory(UserRepository(ApiClient.userService))
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (count!=0) {
            val emailJson: JsonObject = JsonObject()
            emailJson.addProperty("email", s.toString())
            validateEmail(emailJson)
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    private fun validateEmail(email: JsonObject) {
        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) {
                viewModel.validateEmail(email)
            }

            if (result==204)
                binding.signinIdAlertTv.visibility = View.INVISIBLE
            else
                binding.signinIdAlertTv.visibility = View.VISIBLE
        }
    }
}