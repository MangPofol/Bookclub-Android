package com.mangpo.bookclub.view.book_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mangpo.bookclub.databinding.FragmentBookProfileDescBinding

class BookProfileDescFragment : Fragment() {

    private lateinit var binding: FragmentBookProfileDescBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookProfileDescBinding.inflate(inflater, container, false)

        (activity as BookProfileInitActivity).invisibleSkipTV()
        (activity as BookProfileInitActivity).enableNextBtn()

        return binding.root
    }

}