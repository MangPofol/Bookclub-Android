package com.mangpo.bookclub.view.my_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mangpo.bookclub.databinding.ActivityGoalManagementBinding

class GoalManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoalManagementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalManagementBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}