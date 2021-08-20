package com.example.bookclub.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookclub.R
import com.example.bookclub.adapter.BookClubFilterAdapter
import com.example.bookclub.databinding.FragmentBookClubFilterBinding
import com.example.bookclub.model.ClubDTO
import com.example.bookclub.util.HorizontalItemDecorator

class BookClubFilterFragment : Fragment() {
    private lateinit var binding: FragmentBookClubFilterBinding
    private var clubs: List<ClubDTO> = ArrayList<ClubDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookClubFilterBinding.inflate(inflater, container, false)

        clubs = mutableListOf<ClubDTO>(ClubDTO(1L, "북클럽 A"), ClubDTO(2L, "북클럽 B"))

        binding.clubFilterRecyclerView.adapter = BookClubFilterAdapter(clubs)
        binding.clubFilterRecyclerView.addItemDecoration(HorizontalItemDecorator(10))

        return binding.root
    }
}