package com.mangpo.bookclub.view.my_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mangpo.bookclub.databinding.ActivityChecklistManagementBinding

class ChecklistManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChecklistManagementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChecklistManagementBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.backIvView.setOnClickListener {
            finish()
        }
    }
}