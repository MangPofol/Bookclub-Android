package com.mangpo.bookclub.view.auth.signin

import com.google.android.material.chip.Chip
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentGenreBinding
import com.mangpo.bookclub.view.BaseFragment

class GenreFragment : BaseFragment<FragmentGenreBinding>(FragmentGenreBinding::inflate) {
    private val clickedGenres: MutableList<String> = ArrayList<String>()
    private val genres = listOf<String>(
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

    override fun initAfterBinding() {
        createChips()
        (requireActivity() as SignInActivity).changeToolbarText(null, getString(R.string.action_skip), getString(R.string.msg_input_information_display_my_profile))
        validate()
    }

    private fun createChips() {
        for (genre in genres) {
            val chip: Chip = this.layoutInflater.inflate(R.layout.chip_genre, binding.genreCg, false) as Chip
            chip.text = genre
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

                validate()
            }

            binding.genreCg.addView(chip)
        }
    }

    private fun validate() {
        if (binding.genreCg.checkedChipIds.size > 0)
            (requireActivity() as SignInActivity).changeNextButtonState(true)
        else
            (requireActivity() as SignInActivity).changeNextButtonState(false)
    }

    fun getData(): List<String> = clickedGenres
}