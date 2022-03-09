package com.mangpo.bookclub.view.auth.signin

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivitySignInBinding
import com.mangpo.bookclub.model.entities.LoginUser
import com.mangpo.bookclub.model.entities.User
import com.mangpo.bookclub.model.remote.UserResponse
import com.mangpo.bookclub.utils.AuthUtils
import com.mangpo.bookclub.utils.ImgUtils.getAbsolutePathByBitmap
import com.mangpo.bookclub.utils.ImgUtils.uriToBitmap
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.utils.isNetworkAvailable
import com.mangpo.bookclub.view.BaseActivity
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.AuthViewModel
import com.mangpo.bookclub.viewmodel.UserViewModel

class SignInActivity : BaseActivity<ActivitySignInBinding>(ActivitySignInBinding::inflate) {
    private val authVm by viewModels<AuthViewModel>()
    private val userVm by viewModels<UserViewModel>()

    private lateinit var userResponse: UserResponse
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.signInNavHostFragment.id) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.navigation_signin)

        when (intent.getStringExtra("mode")) {
            "ROLE_NEED_EMAIL" -> navGraph.setStartDestination(R.id.emailAuthenticationFragment)    //이메일 인증이 안 된 유저
            "ROLE_NEED_PROFILE" -> navGraph.setStartDestination(R.id.welcomeFragment)    //프로필 정보 입력이 안 된 유저
            else -> navGraph.setStartDestination(R.id.emailPasswordFragment)
        }

        navController.graph = navGraph

        userVm.getUserInfo()
    }

    override fun initAfterBinding() {
        setMyClickListener()
        observe()
    }

    private fun setMyClickListener() {
        binding.signInTbActionTv.setOnClickListener {
            hideKeyboard(binding.root)

            when (findNavController(binding.signInNavHostFragment.id).currentDestination?.id) {
                R.id.expressMeFragment -> {
                    user.introduce = null
                    findNavController(binding.signInNavHostFragment.id).navigate(R.id.action_expressMeFragment_to_genreFragment)
                }

                R.id.genreFragment -> {
                    user.genres = listOf()
                    findNavController(binding.signInNavHostFragment.id).navigate(R.id.action_genreFragment_to_readingStyleFragment)
                }

                R.id.readingStyleFragment -> {
                    user.style = null
                    findNavController(binding.signInNavHostFragment.id).navigate(R.id.action_readingStyleFragment_to_readingGoalFragment)
                }

                R.id.readingGoalFragment -> {
                    user.goal = null
                    updateUser()
                }

                else -> nextStep()
            }
        }

        binding.signInNextBtn.setOnClickListener {
            nextStep()
        }

        binding.signInTb.setNavigationOnClickListener {
            if (findNavController(binding.signInNavHostFragment.id).currentDestination?.id == R.id.emailPasswordFragment)
                finish()
            else
                findNavController(binding.signInNavHostFragment.id).popBackStack()
        }
    }

    private fun nextStep() {
        if (!isNetworkAvailable(this))
            showSnackBar(getString(R.string.error_check_network))
        else {
            when (findNavController(binding.signInNavHostFragment.id).currentDestination?.id) {
                R.id.emailPasswordFragment -> {
                    user =
                        (supportFragmentManager.findFragmentById(binding.signInNavHostFragment.id)?.childFragmentManager?.fragments?.get(
                            0
                        ) as EmailPasswordFragment).getUser()
                    signIn(user)
                }

                R.id.emailAuthenticationFragment -> {
                    val code: Int =
                        (supportFragmentManager.findFragmentById(binding.signInNavHostFragment.id)?.childFragmentManager?.fragments?.get(
                            0
                        ) as EmailAuthenticationFragment).getEmailCode()
                    authVm.validateEmailSendCode(code)
                }

                R.id.welcomeFragment -> findNavController(binding.signInNavHostFragment.id).navigate(
                    R.id.action_welcomeFragment_to_photoNicknameFragment
                )

                R.id.photoNicknameFragment -> {
                    if (!::user.isInitialized) {
                        user = User()
                        user.email = AuthUtils.getEmail()
                        user.password = AuthUtils.getPassword()
                    }

                    user.profileImgLocation =
                        (supportFragmentManager.findFragmentById(binding.signInNavHostFragment.id)?.childFragmentManager?.fragments?.get(
                            0
                        ) as PhotoNicknameFragment).getProfileImg()
                    user.nickname =
                        (supportFragmentManager.findFragmentById(binding.signInNavHostFragment.id)?.childFragmentManager?.fragments?.get(
                            0
                        ) as PhotoNicknameFragment).getNickname()
                    findNavController(binding.signInNavHostFragment.id).navigate(R.id.action_photoNicknameFragment_to_genderBirthFragment)
                }

                R.id.genderBirthFragment -> {
                    user.sex =
                        (supportFragmentManager.findFragmentById(binding.signInNavHostFragment.id)?.childFragmentManager?.fragments?.get(
                            0
                        ) as GenderBirthFragment).getGender()
                    user.birthdate =
                        (supportFragmentManager.findFragmentById(binding.signInNavHostFragment.id)?.childFragmentManager?.fragments?.get(
                            0
                        ) as GenderBirthFragment).getBirth()
                    findNavController(binding.signInNavHostFragment.id).navigate(R.id.action_genderBirthFragment_to_expressMeFragment)
                }

                R.id.expressMeFragment -> {
                    user.introduce =
                        (supportFragmentManager.findFragmentById(binding.signInNavHostFragment.id)?.childFragmentManager?.fragments?.get(
                            0
                        ) as ExpressMeFragment).getData()
                    findNavController(binding.signInNavHostFragment.id).navigate(R.id.action_expressMeFragment_to_genreFragment)
                }

                R.id.genreFragment -> {
                    user.genres =
                        (supportFragmentManager.findFragmentById(binding.signInNavHostFragment.id)?.childFragmentManager?.fragments?.get(
                            0
                        ) as GenreFragment).getData()
                    findNavController(binding.signInNavHostFragment.id).navigate(R.id.action_genreFragment_to_readingStyleFragment)
                }

                R.id.readingStyleFragment -> {
                    user.style =
                        (supportFragmentManager.findFragmentById(binding.signInNavHostFragment.id)?.childFragmentManager?.fragments?.get(
                            0
                        ) as ReadingStyleFragment).getData()
                    LogUtil.d("SignInActivity", "nextStep readingStyleFragment user -> $user")
                    findNavController(binding.signInNavHostFragment.id).navigate(R.id.action_readingStyleFragment_to_readingGoalFragment)
                }

                R.id.readingGoalFragment -> {
                    user.goal =
                        (supportFragmentManager.findFragmentById(binding.signInNavHostFragment.id)?.childFragmentManager?.fragments?.get(
                            0
                        ) as ReadingGoalFragment).getData()

                    updateUser()
                }
            }
        }
    }

    private fun updateUser() {
        if (!isNetworkAvailable(this))
            showSnackBar(getString(R.string.error_check_network))
        else if (user.profileImgLocation!=null)
            userVm.uploadImgFile(getAbsolutePathByBitmap(applicationContext, uriToBitmap(applicationContext, Uri.parse(user.profileImgLocation))))
        else
            userVm.updateUser(user, userResponse.userId)
    }

    private fun bindUserResponseToUser() {
        userResponse.sex = user.sex!!
        userResponse.birthdate = user.birthdate!!
        userResponse.nickname = user.nickname!!
        userResponse.introduce = user.introduce
        userResponse.style = user.style
        userResponse.goal = user.goal
        userResponse.profileImgLocation = user.profileImgLocation
        userResponse.genres = user.genres
    }

    private fun observe() {
        authVm.signUpCode.observe(this, Observer {
            LogUtil.d("SignInActivity", "signUpCode Observe! signUpCode: $it")
            if (it == 201) {
                authVm.login(LoginUser(user.email!!, user.password!!))
            } else
                showSnackBar(getString(R.string.error_api))
        })

        authVm.loginSuccess.observe(this, Observer {
            if (it) {
                userVm.getUserInfo()
                findNavController(binding.signInNavHostFragment.id).navigate(R.id.action_emailPasswordFragment_to_emailAuthenticationFragment)
            } else
                showSnackBar(getString(R.string.error_api))
        })

        userVm.updateUserCode.observe(this, Observer {
            val code = it.getContentIfNotHandled()
            LogUtil.d("SignInActivity", "updateUserCode Observe! updateUserCode: $code")

            if (code!=null) {
                when (code) {
                    204 -> {
                        showToast(getString(R.string.msg_success_sign_in))
                        startActivityWithClear(MainActivity::class.java)
                    }
                    else -> showSnackBar(getString(R.string.error_api))
                }
            }
        })

        userVm.getUserCode.observe(this, Observer {
            val code = it.getContentIfNotHandled()

            if (code != null) {
                when (code) {
                    200 -> userResponse = userVm.getUser()!!
                    else -> Snackbar.make(
                        binding.root,
                        getString(R.string.error_api),
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(getString(R.string.action_retry)) {
                        userVm.getUserInfo()
                    }
                }
            }
        })

        userVm.uploadImgFileCode.observe(this, Observer {
            val code = it.getContentIfNotHandled()
            if (code!=null) {
                when (code) {
                    200 -> {
                        user.profileImgLocation = userVm.getImgPath()
                        userVm.updateUser(user, userResponse.userId)
                    }
                    else -> {
                        dismissLoadingDialog()
                        showSnackBar(getString(R.string.error_api))
                    }
                }
            }
        })
    }

    fun changeToolbarText(title: String?, action: String?, desc: String?) {
        if (title == null)
            binding.signInTb.title = ""
        else
            binding.signInTb.title = title

        if (action == null)
            binding.signInTbActionTv.visibility = View.INVISIBLE
        else {
            binding.signInTbActionTv.visibility = View.VISIBLE
            binding.signInTbActionTv.text = action
        }

        if (desc == null)
            binding.signInDescTv.visibility = View.INVISIBLE
        else {
            binding.signInDescTv.text = desc
            binding.signInDescTv.visibility = View.VISIBLE
        }
    }

    fun changeNextButtonState(isEnabled: Boolean) {
        binding.signInNextBtn.isEnabled = isEnabled
    }

    fun changeActionTextViewState(isEnabled: Boolean) {
        binding.signInTbActionTv.isEnabled = isEnabled
    }

    fun signIn(user: User) {
        authVm.signUp(user)
    }
}