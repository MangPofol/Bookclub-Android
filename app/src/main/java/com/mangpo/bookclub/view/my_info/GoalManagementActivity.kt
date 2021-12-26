package com.mangpo.bookclub.view.my_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.lifecycle.Observer
import com.mangpo.bookclub.databinding.ActivityGoalManagementBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class GoalManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoalManagementBinding
    private lateinit var user: UserModel

    private val mainVm: MainViewModel by viewModel()
    private val periodNumList = (0..30).toList().map { it.toString() }
    private val periodUnitList = listOf<String>("년", "개월", "일")
    private val bookCntList = (0..100).toList().map { it.toString() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoalManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNumberPicker()
        observe()
        getUser()

        binding.backIvView.setOnClickListener {
            finish()
        }

        binding.completeTv.setOnClickListener {
            user.goal = getGoal()
            updateGoal(user)
        }
    }

    private fun initNumberPicker() {
        binding.periodNumberNp.displayedValues = periodNumList.toTypedArray()
        binding.periodNumberNp.minValue = 0
        binding.periodNumberNp.maxValue = periodNumList.size - 1
        binding.periodNumberNp.wrapSelectorWheel = true
        binding.periodNumberNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        binding.periodUnitNp.displayedValues = periodUnitList.toTypedArray()
        binding.periodUnitNp.minValue = 0
        binding.periodUnitNp.maxValue = periodUnitList.size - 1
        binding.periodUnitNp.wrapSelectorWheel = true
        binding.periodUnitNp.value = 1
        binding.periodUnitNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        binding.bookCntNp.displayedValues = bookCntList.toTypedArray()
        binding.bookCntNp.minValue = 0
        binding.bookCntNp.maxValue = bookCntList.size - 1
        binding.bookCntNp.wrapSelectorWheel = true
        binding.bookCntNp.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
    }

    private fun getUser() {
        CoroutineScope(Dispatchers.IO).launch {
            mainVm.getUser()
        }
    }

    private fun getGoal(): String =
        "${periodNumList[binding.periodNumberNp.value]}${periodUnitList[binding.periodUnitNp.value]} 동안 ${bookCntList[binding.bookCntNp.value]}권의 책을 기록하기"

    private fun updateGoal(user: UserModel) {
        CoroutineScope(Dispatchers.IO).launch {
            mainVm.updateUser(user)
        }
    }

    private fun observe() {
        mainVm.user.observe(this, Observer {
            user = it
        })

        mainVm.updateUserCode.observe(this, Observer {
            when (it) {
                204 -> {
                    Toast.makeText(this, "독서 목표가 수정되었습니다!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else -> Toast.makeText(this, "오류가 발생했습니다. 다시 한 번 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }
}