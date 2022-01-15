package com.mangpo.bookclub.view.my_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.mangpo.bookclub.databinding.ActivityPasswordReissueBinding
import com.mangpo.bookclub.view.dialog.TempPwDialogFragment
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasswordReissueActivity : AppCompatActivity(), TextWatcher {
    private lateinit var binding: ActivityPasswordReissueBinding

    private val mainVm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordReissueBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe()

        binding.emailEt.addTextChangedListener(this)

        binding.sendBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                mainVm.sendTempPWEmail(binding.emailEt.text.toString())
            }

            TempPwDialogFragment().show(supportFragmentManager, null)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("PasswordReissueActivity", "onTextChanged -> $s")
        val emailJson: JsonObject = JsonObject()
        emailJson.addProperty("email", s.toString())
        validateEmail(emailJson)
    }

    override fun afterTextChanged(s: Editable?) {

    }

    private fun validateEmail(emailJson: JsonObject) {
        CoroutineScope(Dispatchers.Main).launch {
            mainVm.validateEmail(emailJson)
        }
    }

    private fun observe() {
        mainVm.emailAlertVisibility.observe(this, Observer {
            if (it == 204) {
                binding.emailErrTv.visibility = View.VISIBLE
                binding.sendBtn.isEnabled = false
            } else {
                binding.emailErrTv.visibility = View.INVISIBLE
                binding.sendBtn.isEnabled = true
            }
        })
    }
}