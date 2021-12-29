package com.mangpo.bookclub.view.book_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentSetGenreBinding

class SetGenreFragment : Fragment() {

    private lateinit var binding: FragmentSetGenreBinding

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetGenreBinding.inflate(inflater, container, false)

        (activity as BookProfileInitActivity).visibleSkipTV()
        (activity as BookProfileInitActivity).unEnableNextBtn()

        for (genre in genres) {
            val chip: Chip = inflater.inflate(R.layout.genre_chip, container, false) as Chip
            chip.text = genre
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

                if (binding.genreCg.checkedChipIds.size > 0)
                    (activity as BookProfileInitActivity).enableNextBtn()
                else
                    (activity as BookProfileInitActivity).unEnableNextBtn()
            }
            binding.genreCg.addView(chip)
        }

        return binding.root
    }

    fun getGenres(): List<String> = clickedGenres

}