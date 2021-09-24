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
import com.mangpo.bookclub.viewmodel.ClubViewModel

class ClubSelectBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentClubSelectBottomSheetBinding
    private lateinit var clubAdapter: ClubAdapter

    private val clubViewModel: ClubViewModel by activityViewModels<ClubViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("ClubSelectBottomSheet", "onCreate")

        //bottom sheet 모서리 둥글게 디자인
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_Demo_BottomSheetDialog)

        if (clubViewModel.clubs.value==null) {
            Log.e("ClubSelectBottomSheet", "클럽이 존재하지 않습니다.")
        } else {
            Log.e("ClubSelectBottomSheet", clubViewModel.clubs.value.toString())
            clubAdapter = ClubAdapter(clubViewModel.clubs.value!!)
        }

        /*CoroutineScope(Dispatchers.Main).launch {
            Log.e("ClubSelectBottomSheet", "clubs -> ${clubViewModel.clubs.value}")
        }*/

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClubSelectBottomSheetBinding.inflate(inflater, container, false)
        binding.clubRv.adapter = clubAdapter
        binding.clubRv.layoutManager = LinearLayoutManager(this.context)

        return binding.root
    }
}