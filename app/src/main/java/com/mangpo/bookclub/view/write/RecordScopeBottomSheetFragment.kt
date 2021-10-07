package com.mangpo.bookclub.view.write

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentRecordScopeBottomSheetBinding
import com.mangpo.bookclub.model.ClubModel
import com.mangpo.bookclub.view.adapter.OnItemClick
import com.mangpo.bookclub.view.adapter.RecordScopeClubAdapter
import com.mangpo.bookclub.viewmodel.ClubViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel

class RecordScopeBottomSheetFragment(val isSuccess: (Boolean) -> Unit) : BottomSheetDialogFragment(), OnItemClick {
    private lateinit var binding: FragmentRecordScopeBottomSheetBinding
    private lateinit var recordScopeClubAdapter: RecordScopeClubAdapter

    private val clubViewModel: ClubViewModel by activityViewModels<ClubViewModel>()
    private val postViewModel: PostViewModel by activityViewModels<PostViewModel>()

    private var clubs: MutableList<ClubModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("RecordScope", "onCreate")

        clubs = clubViewModel.clubs.value!!
        recordScopeClubAdapter = RecordScopeClubAdapter(clubs, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //어댑터 설정
        binding = FragmentRecordScopeBottomSheetBinding.inflate(inflater, container, false)
        binding.clubRv.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.clubRv.adapter = recordScopeClubAdapter

        //내 서재 체크박스가 체크되면 scope 를 PRIVATE 로 바꾼다.
        binding.myLibraryLayout.clubCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (postViewModel.temporaryPost.value!!.clubIdListForScope.size!=0)
                    postViewModel.temporaryPost.value!!.scope = "뭐로 해야할지 모름"
                else
                    postViewModel.temporaryPost.value!!.scope = "PRIVATE"
            } else {
                if (postViewModel.temporaryPost.value!!.clubIdListForScope.size!=0)
                    postViewModel.temporaryPost.value!!.scope = "CLUB"
                else
                    postViewModel.temporaryPost.value!!.scope = ""
            }
        }

        //기록하기 버튼 클릭 리스너
        binding.recordBtn.setOnClickListener {
            if (postViewModel.temporaryPost.value!!.scope == "")
                Toast.makeText(requireContext(), "공개 범위를 체크해주세요.", Toast.LENGTH_SHORT).show()
            else {
                isSuccess(true)
                dismiss()
            }
        }

        return binding.root
    }

    //클럽 체크박스 클릭 리스너
    override fun onClick(position: Int) {
        //체크됐으면 clubList에 체크된 클럽의 Id를 추가하고, 체크 해제됐으면 삭제한다.
        if (postViewModel.temporaryPost.value!!.clubIdListForScope.contains(clubs[position].id!!))
            postViewModel.temporaryPost.value!!.clubIdListForScope.remove(clubs[position].id!!)
        else
            postViewModel.temporaryPost.value!!.clubIdListForScope.add(clubs[position].id!!)

        //내 서재를 포함한 모든 체크박스가 체크됐으면 PUBLIC, 아니면 CLUB 이다.
        if (binding.myLibraryLayout.clubCb.isChecked && postViewModel.temporaryPost.value!!.clubIdListForScope.size==clubs.size)
            postViewModel.temporaryPost.value!!.scope = "PUBLIC"
        else if (!binding.myLibraryLayout.clubCb.isChecked && postViewModel.temporaryPost.value!!.clubIdListForScope.size==0)
            postViewModel.temporaryPost.value!!.scope = ""
        else if (binding.myLibraryLayout.clubCb.isChecked && postViewModel.temporaryPost.value!!.clubIdListForScope.size==0)
            postViewModel.temporaryPost.value!!.scope = "PRIVATE"
        else
            postViewModel.temporaryPost.value!!.scope = "CLUB"
    }

    //bottom sheet dialog 위쪽 모서리 둥글게 하는 함수
    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }
}