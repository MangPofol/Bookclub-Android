package com.mangpo.bookclub.view.book_profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityBookProfileInitBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.view.LoginActivity
import com.mangpo.bookclub.viewmodel.MainViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel
import gun0912.tedimagepicker.util.ToastUtil.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookProfileInitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookProfileInitBinding
    private lateinit var mPreferences: SharedPreferences
    private lateinit var user: UserModel

    private val mainVm: MainViewModel by viewModel()
    private val postVm: PostViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookProfileInitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindUser()

        binding.backIvView.setOnClickListener { //뒤로가기 이미지 버튼 클릭 리스너
            backFragment()
        }

        binding.nextBtn.outlineProvider = null  //다음 버튼 그림자 제거

        //frameLayout의 초기 화면 설정
        supportFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, BookProfileDescFragment()).commitAllowingStateLoss()

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
                    .replace(binding.frameLayout.id, BookProfileDescFragment())
                    .commitAllowingStateLoss()
            }
            SetGenderAndBirthFragment::class.java -> {
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, SetNicknameFragment())
                    .commitAllowingStateLoss()
            }
            ExpressMeFragment::class.java -> {
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, SetGenderAndBirthFragment())
                    .commitAllowingStateLoss()
            }
            SetGenreFragment::class.java -> {
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, ExpressMeFragment()).commitAllowingStateLoss()
            }
            ReadingStyleFragment::class.java -> {
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, SetGenreFragment()).commitAllowingStateLoss()
            }
            ReadingGoalFragment::class.java -> {
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, ReadingStyleFragment())
                    .commitAllowingStateLoss()
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

    private fun signIn() {
        CoroutineScope(Dispatchers.Main).launch {
            //프로필 이미지가 있으면 우선 프로필 이미지부터 업로드한다.
            if (user.profileImgLocation != "") {
                val url = postVm.uploadImg(user.profileImgLocation)
                if (url == null) {
                    Toast.makeText(
                        this@BookProfileInitActivity,
                        "이미지 업로드 중 오류 발생. 다시 시도해 주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                } else {
                    mainVm.createUser()
                }
            } else {
                mainVm.createUser()
            }

            if (mainVm.getNewUser() == null) {
                Toast.makeText(context, "회원가입 실패. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            } else {
                //회원가입이 완료되면 사용자가 직접 로그인하도록 LoginActivity 로 이동.
                Toast.makeText(context, "회원가입 완료", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@BookProfileInitActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun goToNextStep(isSkip: Boolean) {
        user = mainVm.getNewUser()

        when (supportFragmentManager.fragments[0].javaClass) {
            BookProfileDescFragment::class.java -> {
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, SetNicknameFragment())
                    .commitAllowingStateLoss()
            }
            SetNicknameFragment::class.java -> {
                user.profileImgLocation =
                    (supportFragmentManager.fragments[0] as SetNicknameFragment).getProfileImg()
                user.nickname =
                    (supportFragmentManager.fragments[0] as SetNicknameFragment).getNickname()
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, SetGenderAndBirthFragment())
                    .commitAllowingStateLoss()
            }
            SetGenderAndBirthFragment::class.java -> {
                user.sex =
                    (supportFragmentManager.fragments[0] as SetGenderAndBirthFragment).getGender()
                user.birthdate =
                    (supportFragmentManager.fragments[0] as SetGenderAndBirthFragment).getBirth()
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, ExpressMeFragment()).commitAllowingStateLoss()
            }
            ExpressMeFragment::class.java -> {
                if (!isSkip)
                    user.introduce =
                        (supportFragmentManager.fragments[0] as ExpressMeFragment).getExpressText()
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, SetGenreFragment()).commitAllowingStateLoss()
            }
            SetGenreFragment::class.java -> {
                if (!isSkip)
                    user.genres =
                        (supportFragmentManager.fragments[0] as SetGenreFragment).getGenres()
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, ReadingStyleFragment())
                    .commitAllowingStateLoss()
            }
            ReadingStyleFragment::class.java -> {
                if (!isSkip)
                    user.style =
                        (supportFragmentManager.fragments[0] as ReadingStyleFragment).getReadingStyle()
                supportFragmentManager.beginTransaction()
                    .replace(binding.frameLayout.id, ReadingGoalFragment())
                    .commitAllowingStateLoss()
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