package com.example.bookclub.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bookclub.databinding.FragmentSortFilterBinding

class SortFilterFragment : Fragment() {
    private lateinit var binding: FragmentSortFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSortFilterBinding.inflate(inflater, container, false)

        return binding.root
    }
}