package com.example.bookclub.view.write

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookclub.R
import com.example.bookclub.databinding.FragmentWriteBinding
import com.example.bookclub.view.MainActivity

class WriteFragment : Fragment() {
    private lateinit var binding: FragmentWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setDrawer(binding.toolbar)   //navigation drawer 등록
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_more_vert_36_black)  //navigation icon 설정
    }

}