package com.mangpo.bookclub.view.book_profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityBookProfileInitBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.MainViewModel
import gun0912.tedimagepicker.util.ToastUtil.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookProfileInitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookProfileInitBinding
    private lateinit var mPreferences: SharedPreferences
    private lateinit var user: UserModel

    private val mainVm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookProfileInitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observe()
        bindUser()

        binding.backIvView.setOnClickListener { //뒤로가기 이미지 버튼 클릭 리스너
            backFragment()
        }

        binding.nextBtn.outlineProvider = null  //다음 버튼 그림자 제거

        //frameLayout의 초기 화면 설정
        supportFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, BookProfileDescFragment()).commit()

        binding.nextBtn.setOnClickListener {    //다음 버튼 클릭 리스너
            goToNextStep(false)
        }

        binding.skipTv.setOnClickListener {
            goToNextStep(true)
        }
    }

    private fun bindUser() {
        mPreferences = getSharedPreferences("signInPreferences", AppCompatActivity.MODE_PRIVATE)
        user = Gson().fromJson(mPreferences.getString("newUser", "null")!!, UserModel::class.java)
        mainVm.updateNewUser(user)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        backFragment()
    }

    fun unEnableNextBtn() {
        binding.nextBtn.isEnabled = false
        binding.nextBtn.setTextColor(ContextCompat.getColor(context, R.color.grey12))
        binding.nextBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.grey13))
    }

    fun enableNextBtn() {
        binding.nextBtn.isEnabled = true
        binding.nextBtn.setTextColor(ContextCompat.getColor(context, R.color.white))
        binding.nextBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red))
    }

    private fun backFragment() {
        when (supportFragmentManager.fragments[0].javaClass) {
            BookProfileDescFragment::class.java -> {
                finish()
            }
            SetNicknameFragment::class.java -> {
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, BookProfileDescFragment()).commit()
            }
            SetGenderAndBirthFragment::class.java -> {
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, SetNicknameFragment()).commit()
            }
            ExpressMeFragment::class.java -> {
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, SetGenderAndBirthFragment()).commit()
            }
            SetGenreFragment::class.java -> {
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, ExpressMeFragment()).commit()
            }
            ReadingStyleFragment::class.java -> {
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, SetGenreFragment()).commit()
            }
            ReadingGoalFragment::class.java -> {
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, ReadingStyleFragment()).commit()
            }
        }
    }

    fun visibleSkipTV() {
        binding.skipTv.visibility = View.VISIBLE
    }

    fun invisibleSkipTV() {
        binding.skipTv.visibility = View.INVISIBLE
    }

    fun setKeyboardStatePan() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    fun setKeyboardStateResize() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun goToMain() {
        val intent: Intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun signIn() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                mainVm.createUser()
            }

            if (mainVm.getNewUser() == null) {
                Toast.makeText(context, "회원가입 실패. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "회원가입 완료", Toast.LENGTH_SHORT).show()
                login()
            }
        }
    }

    private fun login() {
        val loginReq: JsonObject = JsonObject()
        loginReq.addProperty("email", user.email)
        loginReq.addProperty("password", user.password)

        CoroutineScope(Dispatchers.Main).launch {
            mainVm.login(loginReq)
        }
    }

    private fun observe() {
        mainVm.loginCode.observe(this, Observer {
            when (it) {
                200 -> {
                    mainVm.updateNewUser(UserModel())
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

    private fun goToNextStep(isSkip: Boolean) {
        user = mainVm.getNewUser()

        when (supportFragmentManager.fragments[0].javaClass) {
            BookProfileDescFragment::class.java -> {
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, SetNicknameFragment()).commit()
            }
            SetNicknameFragment::class.java -> {
                user.nickname =
                    (supportFragmentManager.fragments[0] as SetNicknameFragment).getNickname()
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, SetGenderAndBirthFragment()).commit()
            }
            SetGenderAndBirthFragment::class.java -> {
                user.sex =
                    (supportFragmentManager.fragments[0] as SetGenderAndBirthFragment).getGender()
                user.birthdate =
                    (supportFragmentManager.fragments[0] as SetGenderAndBirthFragment).getBirth()
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, ExpressMeFragment()).commit()
            }
            ExpressMeFragment::class.java -> {
                if (!isSkip)
                    user.introduce =
                        (supportFragmentManager.fragments[0] as ExpressMeFragment).getExpressText()
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, SetGenreFragment()).commit()
            }
            SetGenreFragment::class.java -> {
                if (!isSkip)
                    user.genres =
                        (supportFragmentManager.fragments[0] as SetGenreFragment).getGenres()
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, ReadingStyleFragment()).commit()
            }
            ReadingStyleFragment::class.java -> {
                if (!isSkip)
                    user.style =
                        (supportFragmentManager.fragments[0] as ReadingStyleFragment).getReadingStyle()
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, ReadingGoalFragment()).commit()
            }
            ReadingGoalFragment::class.java -> {
                if (!isSkip)
                    user.goal =
                        (supportFragmentManager.fragments[0] as ReadingGoalFragment).getGoal()
                signIn()
            }
        }

        mainVm.updateNewUser(user)
    }
}