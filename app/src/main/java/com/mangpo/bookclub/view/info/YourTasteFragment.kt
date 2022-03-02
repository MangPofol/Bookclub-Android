package com.mangpo.bookclub.view.info

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentYourTasteBinding
import com.mangpo.bookclub.model.entities.User
import com.mangpo.bookclub.model.remote.UserResponse
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.viewmodel.UserViewModel

class YourTasteFragment : BaseFragment<FragmentYourTasteBinding>(FragmentYourTasteBinding::inflate), TextWatcher {
    private val args: YourTasteFragmentArgs by navArgs()
    private val userVm: UserViewModel by viewModels<UserViewModel>()

    private var clickedGenres: ArrayList<String> = arrayListOf()

    private lateinit var user: UserResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = Gson().fromJson(args.user, UserResponse::class.java)
        clickedGenres = user.genres as ArrayList<String>
    }

    override fun initAfterBinding() {
        (requireActivity() as InfoActivity).changeToolbar(getString(R.string.title_your_taste), true)
        setMyEventClickListener()
        setGenreView()
        observe()

        if (user.style!=null)
            setStyleView(user.style!!)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when {
            p0?.length!=0 -> binding.styleRb4.isChecked = true
            binding.yourTasteReadingStyleRg.checkedRadioButtonId!=-1 -> binding.styleRb4.isChecked = false
            else -> {
                binding.styleRb4.isChecked = false
                binding.yourTasteReadingStyleRg.clearCheck()
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun setMyEventClickListener() {
        binding.yourTasteReadingStyleRg.setOnCheckedChangeListener { radioGroup, i ->
            when (radioGroup.checkedRadioButtonId) {
                binding.styleRb1.id, binding.styleRb2.id, binding.styleRb3.id -> {
                    hideKeyboard()

                    binding.yourTasteStyleEt.setText("")
                    binding.yourTasteStyleEt.clearFocus()
                }
            }
        }

        binding.yourTasteStyleEt.addTextChangedListener(this)
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

        binding.yourTasteGenreCg.removeAllViews()
        for (genre in genres) {
            val chip: Chip =
                layoutInflater.inflate(R.layout.chip_genre, binding.yourTasteGenreCg, false) as Chip
            chip.text = genre

            if (clickedGenres.contains(genre)) {
                chip.isChecked = true
                chip.setChipBackgroundColorResource(R.color.primary)
                chip.setChipStrokeColorResource(R.color.transparent)
            }

            chip.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    (buttonView as Chip).setChipBackgroundColorResource(R.color.primary)
                    (buttonView as Chip).setChipStrokeColorResource(R.color.transparent)
                    clickedGenres.add(chip.text.toString())
                } else {
                    (buttonView as Chip).setChipBackgroundColorResource(R.color.white)
                    (buttonView as Chip).setChipStrokeColorResource(R.color.grey_dark)
                    clickedGenres.remove(chip.text.toString())
                }
            }

            binding.yourTasteGenreCg.addView(chip)
        }

    }

    private fun setStyleView(style: String) {
        when (style) {
            getString(R.string.title_planner) -> binding.styleRb1.isChecked = true
            getString(R.string.title_time_to_time) -> binding.styleRb2.isChecked = true
            getString(R.string.title_writer) -> binding.styleRb3.isChecked = true
            else -> {
                binding.styleRb4.isChecked = true
                binding.yourTasteStyleEt.setText(style)
            }
        }
    }

    private fun userResponseToUser(userResponse: UserResponse): User {
        val user = User(
            email = userResponse.email,
            nickname = userResponse.nickname,
            sex = userResponse.sex,
            birthdate = userResponse.birthdate,
            introduce = userResponse.introduce,
            goal = userResponse.goal,
            profileImgLocation = userResponse.profileImgLocation,
            genres = clickedGenres
        )

        when (binding.yourTasteReadingStyleRg.checkedRadioButtonId) {
            R.id.style_rb1 -> user.style = binding.styleRb1.text.toString()
            R.id.style_rb2 -> user.style = binding.styleRb2.text.toString()
            R.id.style_rb3 -> user.style = binding.styleRb3.text.toString()
            else -> user.style = binding.yourTasteStyleEt.text.toString()
        }

        return user
    }

    private fun observe() {
        userVm.updateUserCode.observe(viewLifecycleOwner, Observer {
            var code: Int? = if (it.hasBeenHandled)
                it.peekContent()
            else
                it.getContentIfNotHandled()

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