package com.mangpo.bookclub.view.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mangpo.bookclub.databinding.FragmentMyBookClubBinding
import com.mangpo.bookclub.viewmodel.ClubViewModel

class MyBookClubFragment : Fragment() {

    private lateinit var binding: FragmentMyBookClubBinding
    private val clubViewModel: ClubViewModel by activityViewModels<ClubViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MyBookClub", "onCreate")

        /*CoroutineScope(Dispatchers.Main).launch {
            clubViewModel.getClubsByUser()
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("MyBookClub", "onCreateView")

        binding = FragmentMyBookClubBinding.inflate(inflater, container, false)

        if (clubViewModel.clubs.value!!.size == 0)
            parentFragmentManager.beginTransaction()
                .replace(binding.myBookClubFrameLayout.id, MyBookClubNoClubFragment())
                .commitAllowingStateLoss()
        else
            parentFragmentManager.beginTransaction()
                .replace(binding.myBookClubFrameLayout.id, MyBookClubListFragment())
                .commitAllowingStateLoss()

        return binding.root
    }

}