package com.example.bookclub.view.write

import android.os.Bundle
import android.util.Log
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

        binding.frameLayout.removeAllViewsInLayout()
        //기본 화면을 기록하기 화면으로 설정
        childFragmentManager.beginTransaction().add(binding.frameLayout.id, RecordFragment())
            .addToBackStack("record")
            .commit()

        return binding.root
    }

    fun moveToRecord() {
        binding.frameLayout.removeAllViews()
        childFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, RecordFragment()).addToBackStack("record").commit()
    }

    fun moveToSelectBook() {
        binding.frameLayout.removeAllViews()
        childFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, SelectBookFragment()).addToBackStack("selectBook").commit()
    }
}