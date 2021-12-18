package com.mangpo.bookclub.view.my_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityYourTasteBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class YourTasteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityYourTasteBinding
    private lateinit var user: UserModel

    private val mainVm: MainViewModel by viewModel()
    private var clickedGenres: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityYourTasteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe()

        getUser()

        binding.backIvView.setOnClickListener {
            finish()
        }
    }

    private fun setGenreView() {
        val genres = listOf<String>(
            "소설",
            "시",
            "에세이",
            "경제/경영",
            "자기계발",
            "인문학",
            "역사/문화",
            "종교",
            "정치/사회",
            "여행",
            "만화",
            "사회과학",
            "역사",
            "예술/대중문화",
            "과학",
            "기술/공학",
            "컴퓨터/IT",
            "기타"
        )

        for (genre in genres) {
            val chip: Chip =
                layoutInflater.inflate(R.layout.genre_chip, binding.genreCg, false) as Chip
            chip.text = genre

            if (clickedGenres.contains(genre)) {
                chip.isChecked = true
                chip.setChipBackgroundColorResource(R.color.main_blue)
                chip.setChipStrokeColorResource(R.color.transparent)
            }

            chip.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    (buttonView as Chip).setChipBackgroundColorResource(R.color.main_blue)
                    (buttonView as Chip).setChipStrokeColorResource(R.color.transparent)
                    clickedGenres.add(chip.text.toString())
                } else {
                    (buttonView as Chip).setChipBackgroundColorResource(R.color.white)
                    (buttonView as Chip).setChipStrokeColorResource(R.color.grey12)
                    clickedGenres.remove(chip.text.toString())
                }
            }

            binding.genreCg.addView(chip)
        }

    }

    private fun setStyleView(style: String) {
        when (style) {
            "독서 시간이 정해져 있는 계획파" -> binding.styleRb1.isChecked = true
            "자투리 시간에 책을 읽는 틈틈이파" -> binding.styleRb2.isChecked = true
            "열심히 기록하며 보는 끄적파" -> binding.styleRb3.isChecked = true
            else -> {
                binding.styleEt.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.main_blue
                    )
                )
                binding.styleEt.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
                binding.styleEt.setText(style)
            }
        }
    }

    private fun getUser() {
        CoroutineScope(Dispatchers.IO).launch {
            mainVm.getUser()
        }
    }

    private fun observe() {
        mainVm.user.observe(this, Observer {
            user = it
            clickedGenres = user.genres as ArrayList<String>
            setGenreView()

            if (user.style != "")
                setStyleView(user.style)
        })
    }

}