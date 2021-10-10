package com.mangpo.bookclub.view.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentMyBookClubBinding
import com.mangpo.bookclub.view.bookclub.CreateClubActivity

class MyBookClubFragment : Fragment() {

    private lateinit var binding: FragmentMyBookClubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBookClubBinding.inflate(inflater, container, false)

        binding.addBookClubBtn.setOnClickListener {
            val intent: Intent = Intent(requireContext(), CreateClubActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

}