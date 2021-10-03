package com.mangpo.bookclub.view.write

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentRecordScopeBottomSheetBinding
import com.mangpo.bookclub.model.ClubModel
import com.mangpo.bookclub.view.adapter.RecordScopeClubAdapter
import com.mangpo.bookclub.viewmodel.ClubViewModel

class RecordScopeBottomSheetFragment() : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentRecordScopeBottomSheetBinding
    private lateinit var recordScopeClubAdapter: RecordScopeClubAdapter

    private val clubViewModel: ClubViewModel by activityViewModels<ClubViewModel>()

    private var clubs: MutableList<ClubModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("RecordScope", "onCreate")

        clubs = clubViewModel.clubs.value!!
        recordScopeClubAdapter = RecordScopeClubAdapter(clubs)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //어댑터 설정
        binding = FragmentRecordScopeBottomSheetBinding.inflate(inflater, container, false)
        binding.clubRv.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.clubRv.adapter = recordScopeClubAdapter

        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }
}