package com.example.bookclub.view.bookclub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookclub.R
import com.example.bookclub.databinding.FragmentClubSelectBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ClubSelectBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentClubSelectBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClubSelectBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }
}