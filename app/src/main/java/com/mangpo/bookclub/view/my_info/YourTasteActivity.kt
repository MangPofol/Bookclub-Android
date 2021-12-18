package com.mangpo.bookclub.view.my_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ActivityYourTasteBinding
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.util.AccountSharedPreference
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class YourTasteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityYourTasteBinding
    private lateinit var user: UserModel

    private var clickedGenres: ArrayList<String> = arrayListOf()

    private val mainVm: MainViewModel by viewModel()
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s?.length != 0) {
                binding.styleRb4.isChecked = true
            }
        }

        override fun afterTextChanged(s: Editable?) {
            binding.styleEt.removeTextChangedListener(this)
            binding.styleEt.addTextChangedListener(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityYourTasteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe()

        getUser()

        binding.completeTv.setOnClickListener {
            setUser()
            updateUser(user)
        }

        binding.backIvView.setOnClickListener {
            finish()
        }

        binding.styleEt.addTextChangedListener(textWatcher)

        binding.styleRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.style_rb1, R.id.style_rb2, R.id.style_rb3 -> {
                    hideKeyBord(group)
                    binding.styleEt.setText("")
                    binding.styleEt.clearFocus()
                }
            }
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

        binding.genreCg.removeAllViews()
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
                binding.styleRb4.isChecked = true
                binding.styleEt.setText(style)
            }
        }
    }

    private fun getUser() {
        CoroutineScope(Dispatchers.IO).launch {
            mainVm.getUser()
        }
    }

    private fun updateUser(user: UserModel) {
        CoroutineScope(Dispatchers.IO).launch {
            mainVm.updateUser(user)
        }
    }

    private fun setUser() {
        when (binding.styleRg.checkedRadioButtonId) {
            R.id.style_rb1 -> user.style = "독서 시간이 정해져 있는 계획파"
            R.id.style_rb2 -> user.style = "자투리 시간에 책을 읽는 틈틈이파"
            R.id.style_rb3 -> user.style = "열심히 기록하며 보는 끄적파"
            R.id.style_rb4 -> user.style = binding.styleEt.text.toString()
        }

        user.password = AccountSharedPreference.getUserPass(applicationContext)
    }

    //올라와 있는 키보드를 내리는 함수
    private fun hideKeyBord(v: View) {
        val imm: InputMethodManager =
            getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    private fun observe() {
        mainVm.user.observe(this, Observer {
            user = it
            clickedGenres = user.genres as ArrayList<String>
            setGenreView()

            if (user.style != "")
                setStyleView(user.style)
        })

        mainVm.updateUserCode.observe(this, Observer {
            when (it) {
                204 -> {
                    Toast.makeText(this, "취향이 수정되었습니다!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else -> Toast.makeText(this, "오류가 발생했습니다. 다시 한 번 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

}