package com.mangpo.bookclub.view.bookclub

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mangpo.bookclub.databinding.FragmentMyBookClubListBinding

//베타 버전 출시 후 사용
class MyBookClubListFragment : Fragment() {

    private lateinit var binding: FragmentMyBookClubListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBookClubListBinding.inflate(inflater, container, false)

        binding.addBookClubBtn.setOnClickListener {
            val intent: Intent = Intent(requireContext(), CreateClubActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}