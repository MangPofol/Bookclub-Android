package com.example.bookclub.view.library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookclub.view.adapter.BookClubFilterAdapter
import com.example.bookclub.databinding.FragmentBookClubFilterBinding
import com.example.bookclub.model.ClubModel
import com.example.bookclub.util.HorizontalItemDecorator

class BookClubFilterFragment : Fragment() {
    private lateinit var binding: FragmentBookClubFilterBinding
    private var clubs: List<ClubModel> = ArrayList<ClubModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookClubFilterBinding.inflate(inflater, container, false)

        clubs = mutableListOf<ClubModel>(ClubModel(1L, "북클럽 A"), ClubModel(2L, "북클럽 B"))

        binding.clubFilterRecyclerView.adapter = BookClubFilterAdapter(clubs)
        binding.clubFilterRecyclerView.addItemDecoration(HorizontalItemDecorator(10))

        return binding.root
    }
}