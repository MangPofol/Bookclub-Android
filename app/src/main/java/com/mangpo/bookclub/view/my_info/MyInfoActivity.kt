package com.mangpo.bookclub.view.my_info

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mangpo.bookclub.databinding.ActivityMyInfoBinding

class MyInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.genreNextIv.setOnClickListener {
            goYourTaste()
        }

        binding.styleNextIv.setOnClickListener {
            goYourTaste()
        }

        binding.goalManagementIv.setOnClickListener {
            goGoalManagement()
        }

        binding.checklistManagementIv.setOnClickListener {
            goChecklistManagement()
        }

        binding.backIvView.setOnClickListener {
            finish()
        }
    }

    //ChecklistManagementActivity 화면으로 이동하는 함수
    private fun goChecklistManagement() {
        startActivity(Intent(this, ChecklistManagementActivity::class.java))
    }

    //GoalManagementActivity 화면으로 이동하는 함수
    private fun goGoalManagement() {
        startActivity(Intent(this, GoalManagementActivity::class.java))
    }

    //YourTasteActivity 화면으로 이동하는 함수
    private fun goYourTaste() {
        startActivity(Intent(this, YourTasteActivity::class.java))
    }
}