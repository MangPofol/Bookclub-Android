package com.mangpo.bookclub.view.bookclub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mangpo.bookclub.databinding.FragmentMyBookClubNoClubBinding

//베타 버전 출시 후 사용
class MyBookClubNoClubFragment : Fragment() {

    private lateinit var binding: FragmentMyBookClubNoClubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBookClubNoClubBinding.inflate(inflater, container, false)

        return binding.root
    }

}