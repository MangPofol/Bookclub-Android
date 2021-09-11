package com.example.bookclub.view.write

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
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

        //기본 화면을 기록하기 화면으로 설정
        childFragmentManager.beginTransaction().add(binding.frameLayout.id, RecordFragment()).commit()

        return binding.root
    }

    fun changeChildFragment() {
        binding.frameLayout.removeAllViewsInLayout()
        childFragmentManager.beginTransaction().add(binding.frameLayout.id, SelectBookFragment()).commit()
    }

}