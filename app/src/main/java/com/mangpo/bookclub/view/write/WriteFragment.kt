package com.mangpo.bookclub.view.write

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mangpo.bookclub.databinding.FragmentWriteBinding

class WriteFragment : Fragment() {
    private lateinit var binding: FragmentWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("WriteFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("WriteFragment", "onCreateView")
        binding = FragmentWriteBinding.inflate(inflater, container, false)

        binding.frameLayout.removeAllViewsInLayout()
        //기본 화면을 기록하기 화면으로 설정
        childFragmentManager.beginTransaction().add(binding.frameLayout.id, RecordFragment())
            .addToBackStack("record")
            .commit()

        return binding.root
    }

    private fun moveToRecord() {
        childFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, RecordFragment()).addToBackStack("record").commit()
    }

    fun moveToSelectBook() {
        binding.frameLayout.removeAllViews()
        childFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, SelectFragment()).addToBackStack("selectBook").commit()
    }

    override fun onPause() {
        super.onPause()
        Log.e("WriteFragment", "onPause")

        moveToRecord()
    }
}