package com.mangpo.bookclub.view.info

import android.os.Bundle
import android.widget.NumberPicker
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentGoalManagementBinding
import com.mangpo.bookclub.model.entities.User
import com.mangpo.bookclub.model.remote.UserResponse
import com.mangpo.bookclub.utils.LogUtil
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.viewmodel.UserViewModel

class GoalManagementFragment : BaseFragment<FragmentGoalManagementBinding>(FragmentGoalManagementBinding::inflate) {
    private val userVm: UserViewModel by viewModels<UserViewModel>()
    private val args: GoalManagementFragmentArgs by navArgs()
    private val periodNumList = (0..30).toList().map { it.toString() }
    private val periodUnitList = listOf<String>("년", "개월", "일")
    private val bookCntList = (0..100).toList().map { it.toString() }

    private lateinit var user: UserResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = Gson().fromJson(args.user, UserResponse::class.java)
    }

    override fun initAfterBinding() {
        (requireActivity() as InfoActivity).changeToolbar(getString(R.string.title_goal_setting), true)
        initNumberPicker()
        observe()

        if (user.goal!=null)
            bindGoal(user.goal!!)
    }

    private fun initNumberPicker() {
        binding.goalMangementPeriodNumberNp.displayedValues = periodNumList.toTypedArray()
        binding.goalMangementPeriodNumberNp.minValue = 0
        binding.goalMangementPeriodNumberNp.maxValue = periodNumList.size - 1
        binding.goalMangementPeriodNumberNp.wrapSelectorWheel = true
        binding.goalMangementPeriodNumberNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        binding.goalManagementPeriodUnitNp.displayedValues = periodUnitList.toTypedArray()
        binding.goalManagementPeriodUnitNp.minValue = 0
        binding.goalManagementPeriodUnitNp.maxValue = periodUnitList.size - 1
        binding.goalManagementPeriodUnitNp.wrapSelectorWheel = true
        binding.goalManagementPeriodUnitNp.value = 1
        binding.goalManagementPeriodUnitNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        binding.goalManagementBookCntNp.displayedValues = bookCntList.toTypedArray()
        binding.goalManagementBookCntNp.minValue = 0
        binding.goalManagementBookCntNp.maxValue = bookCntList.size - 1
        binding.goalManagementBookCntNp.wrapSelectorWheel = true
        binding.goalManagementBookCntNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
    }

    //기존 목표 데이터로 데이터 바인딩하는 함수
    private fun bindGoal(goal: String) {
        val firstWord = goal.split(" ")[0]
        val thirdWord = goal.split(" ")[2]

        val periodNumber = firstWord.replace(("[^\\d.]").toRegex(), "").toInt()
        binding.goalMangementPeriodNumberNp.value = periodNumber

        when {
            firstWord.contains("년") -> binding.goalManagementPeriodUnitNp.value = 0
            firstWord.contains("개월") -> binding.goalManagementPeriodUnitNp.value = 1
            else -> binding.goalManagementPeriodUnitNp.value = 2
        }

        val bookCnt = thirdWord.replace(("[^\\d.]").toRegex(), "").toInt()
        binding.goalManagementBookCntNp.value = bookCnt
    }

    private fun userResponseToUser(userResponse: UserResponse): User {
        val user = User(
            email = userResponse.email,
            nickname = userResponse.nickname,
            sex = userResponse.sex,
            birthdate = userResponse.birthdate,
            introduce = userResponse.introduce,
            profileImgLocation = userResponse.profileImgLocation,
            genres = userResponse.genres,
            style = userResponse.style
        )

        user.goal = getGoal()

        return user
    }

    private fun getGoal(): String? {
        return if (binding.goalMangementPeriodNumberNp.value==0 && binding.goalManagementPeriodUnitNp.value==0 && binding.goalManagementBookCntNp.value==0)
            null
        else
            "${periodNumList[binding.goalMangementPeriodNumberNp.value]}${periodUnitList[binding.goalManagementPeriodUnitNp.value]} 동안 ${bookCntList[binding.goalManagementBookCntNp.value]}권의 책을 기록하기"
    }

    private fun observe() {
        userVm.updateUserCode.observe(viewLifecycleOwner, Observer {
            var code: Int? = if (it.hasBeenHandled)
                it.peekContent()
            else
                it.getContentIfNotHandled()
            LogUtil.d("GoalManagementFragment", "updateUserCode observe! updateUserCode -> $code")

            when (code) {
                204 -> findNavController().popBackStack()
                else -> showSnackBar(getString(R.string.error_api))
            }
        })
    }

    fun updateUser() {
        userVm.updateUser(userResponseToUser(user), user.userId)
    }
}