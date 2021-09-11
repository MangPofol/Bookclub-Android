package com.example.bookclub.view.write

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookclub.R
import com.example.bookclub.databinding.FragmentSelectBookBinding

class SelectBookFragment : Fragment() {
    private lateinit var binding: FragmentSelectBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectBookBinding.inflate(inflater, container, false)
        
        binding.readTypeRG.setOnCheckedChangeListener { group, checkedId ->  }

        return binding.root
    }

}