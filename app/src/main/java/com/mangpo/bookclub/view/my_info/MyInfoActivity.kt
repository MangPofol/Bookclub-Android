package com.mangpo.bookclub.view.my_info

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityMyInfoBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyInfoActivity : AppCompatActivity() {

    private val mainVm: MainViewModel by viewModel()

    private lateinit var binding: ActivityMyInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MyInfoActivity", "onCreate")

        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe()

        getUser()

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

    override fun onResume() {
        super.onResume()
        Log.d("MyInfoActivity", "onResume")

        getUser()
    }

    private fun getUser() {
        CoroutineScope(Dispatchers.IO).launch {
            mainVm.getUser()
        }
    }

    private fun setUI(user: UserModel) {
        binding.nicknameTv.text = user.nickname
        binding.idTv.text = user.email

        if (user.introduce == "")
            binding.introduceTv.text = "나를 한 줄로 표현해보세요."
        else
            binding.introduceTv.text = user.introduce

        if (user.genres.isEmpty()) {
            binding.genreCg.removeAllViews()
            binding.genreCg.visibility = View.INVISIBLE
            binding.selectGenreTv.visibility = View.VISIBLE
        } else {
            binding.genreCg.visibility = View.VISIBLE
            binding.selectGenreTv.visibility = View.INVISIBLE
            addGenreChip(user.genres)
        }

    }

    private fun addGenreChip(genres: List<String>) {
        binding.genreCg.removeAllViews()

        for (genre in genres) {
            val chip: Chip =
                layoutInflater.inflate(R.layout.my_info_genre_chip, binding.genreCg, false) as Chip
            chip.text = genre
            binding.genreCg.addView(chip)
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

    private fun observe() {
        mainVm.user.observe(this, Observer {
            setUI(it)
        })
    }
}