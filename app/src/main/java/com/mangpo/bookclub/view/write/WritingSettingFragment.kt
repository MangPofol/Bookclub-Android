package com.mangpo.bookclub.view.write

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mangpo.bookclub.databinding.FragmentWritingSettingBinding
import com.mangpo.bookclub.viewmodel.PostViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WritingSettingFragment : Fragment() {

    private lateinit var binding: FragmentWritingSettingBinding
    private val postVm: PostViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("WritingSettingFragment", "onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWritingSettingBinding.inflate(inflater, container, false)
        Log.d("WritingSettingFragment", "onCreateView")
        Log.d(
            "WritingSettingFragment",
            "bookId: ${postVm.getBookId()}, imgs: ${postVm.imgs}, title: ${postVm.getTitle()}, content: ${postVm.getContent()}"
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("WritingSettingFragment", "onViewCreated")
    }
}