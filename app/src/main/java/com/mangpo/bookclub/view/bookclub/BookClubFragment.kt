package com.mangpo.bookclub.view.bookclub

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentBookClubBinding
import com.mangpo.bookclub.model.ClubModel
import com.mangpo.bookclub.view.MainActivity
import com.mangpo.bookclub.viewmodel.ClubViewModel
import kotlinx.coroutines.*

class BookClubFragment : Fragment() {
    private lateinit var binding: FragmentBookClubBinding
    private lateinit var bottomSheet: ClubSelectBottomSheetFragment

    private val clubViewModel: ClubViewModel by activityViewModels<ClubViewModel>()

    private lateinit var job: Deferred<Unit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("BookClub", "onCreate")

        //사용자가 소속된 클럽 정보 가져오기
        job = CoroutineScope(Dispatchers.Main).async {
            clubViewModel.getClubsByUser()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("BookClub", "onCreateView")

        binding = FragmentBookClubBinding.inflate(inflater, container, false)
        bottomSheet = ClubSelectBottomSheetFragment()

        //클럽 정보를 받아올 때까지 기다리고, 클럽 소개 부분 화면 구성하기
        CoroutineScope(Dispatchers.Main).launch {
            job.await()

            //클럽 정보를 가져오는 처리가 완료될때까지 기다렸다가 클럽 정보 세팅하기
            if (job.isCompleted) {
                if (clubViewModel.clubs.value!!.size==0) {
                    Log.e("BookClub", "소속된 클럽이 없습니다!")
                } else {
                    Log.e("BookClub", "소속된 클럽이 있습니다!")
                    clubViewModel.updateSelectedClub(0)
                    setClubView(clubViewModel.clubs.value!![0])
                }
            }
        }

        //클럽 변경 시 bottom sheet dialog 띄우기
        binding.toolbar.setOnMenuItemClickListener {
            bottomSheet.show(
                (activity as MainActivity).supportFragmentManager, bottomSheet.tag
            )

            false
        }

        binding.hotContents.check(binding.hotMemoButton.id)   //처음 선택된 라디오버튼: 핫한 메모
        //라디오버튼 체크 이벤트 리스너
        binding.hotContents.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {

            }
        }

        //필터 체크박스 리스너
        val checkBoxListener = CompoundButton.OnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                changeFilterCheckBox(checkBox.id)
            }
        }

        //메인 필터 체크박스 리스너 등록
        binding.searchButton.setOnCheckedChangeListener(checkBoxListener)
        binding.clubMemberButton.setOnCheckedChangeListener(checkBoxListener)
        binding.sortButton.setOnCheckedChangeListener(checkBoxListener)

        //selectedClubIdx가 변경되면 이에 따라 클럽 정보 다시 셋팅
        clubViewModel.selectedClubIdx.observe(viewLifecycleOwner, Observer {
            if (clubViewModel.clubs.value!!.size!=0)
                setClubView(clubViewModel.clubs.value!![it])
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setDrawer(binding.toolbar)   //navigation drawer 등록
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_more_vert_36_white)  //navigation icon 설정
    }

    //체크된 필터 말고는 모두 체크 해제
    private fun changeFilterCheckBox(checkBoxId: Int) {
        when (checkBoxId) {
            binding.searchButton.id -> {
                binding.clubMemberButton.isChecked = false
                binding.sortButton.isChecked = false
            }
            binding.clubMemberButton.id -> {
                binding.searchButton.isChecked = false
                binding.sortButton.isChecked = false
            }
            binding.sortButton.id -> {
                binding.searchButton.isChecked = false
                binding.clubMemberButton.isChecked = false
            }
        }
    }

    fun addClub(club: ClubModel) {
        CoroutineScope(Dispatchers.Main).launch {
            clubViewModel.addClub(club)
            setClubView(club)
        }
    }

    private fun setClubView(club: ClubModel) {
        binding.clubNameTV.text = club.name
        binding.levelBtn.text = "${club.level}단계"
        binding.descriptionTV.text = "\"${club.description}\""
    }
}