package com.mangpo.bookclub.view.bookclub

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentClubSelectBottomSheetBinding
import com.mangpo.bookclub.view.adapter.ClubAdapter
import com.mangpo.bookclub.view.adapter.OnItemClick
import com.mangpo.bookclub.viewmodel.ClubViewModel

class ClubSelectBottomSheetFragment : BottomSheetDialogFragment(), OnItemClick {
    private lateinit var binding: FragmentClubSelectBottomSheetBinding
    private lateinit var clubAdapter: ClubAdapter

    private val clubViewModel: ClubViewModel by activityViewModels<ClubViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("ClubSelectBottomSheet", "onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClubSelectBottomSheetBinding.inflate(inflater, container, false)

        if (clubViewModel.clubs.value!!.size==0) {  //사용자가 속한 클럽이 없음.
        } else {    //사용자가 속한 클럽이 존재.
            clubAdapter = ClubAdapter(clubViewModel.clubs.value!!, clubViewModel.selectedClubIdx.value!!, this)
            binding.clubRv.adapter = clubAdapter
            binding.clubRv.layoutManager = LinearLayoutManager(this.context)
        }

        return binding.root
    }

    override fun onClick(position: Int) {
        clubViewModel.updateSelectedClub(position)
        dismiss()
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }
}