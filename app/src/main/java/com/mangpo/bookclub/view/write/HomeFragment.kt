package com.mangpo.bookclub.view.write

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentHomeBinding
import com.mangpo.bookclub.view.setting.SettingActivity
import com.mangpo.bookclub.viewmodel.PostViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*
import com.mangpo.bookclub.model.CheckListModel
import com.mangpo.bookclub.model.UserModel
import com.mangpo.bookclub.view.adapter.ChecklistContentRVAdapter
import com.mangpo.bookclub.view.MainActivity
import com.mangpo.bookclub.viewmodel.ChecklistViewModel
import com.mangpo.bookclub.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val postVm: PostViewModel by sharedViewModel()
    private val mainVm: MainViewModel by sharedViewModel()
    private val checklistVm: ChecklistViewModel by sharedViewModel()
    private val checklistContentAdapter: ChecklistContentRVAdapter = ChecklistContentRVAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("MainFragment", "onCreateView")

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        initAdapter()
        observe()

        //햄버거 메뉴 클릭 리스너 -> SettingActivity 화면 이동
        binding.hamburgerIb.setOnClickListener {
            val intent: Intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }

        //날짜 표시하기
        val calendar: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("LLLL dd.", Locale.ENGLISH)
        binding.dateTv.text = "The record for ${sdf.format(calendar.time)}"

        //기록 쓰러가기 클릭 리스너 -> RecordFragment 로 이동
        binding.goPostView.setOnClickListener {
            (requireActivity() as MainActivity).moveToRecord(false)
        }

        //프로필 관리 화면으로 이동하는 이미지 클릭 리스너 -> MyInfoActivity 화면으로 이동
        binding.goMyInfoClickView.setOnClickListener {
            (requireActivity() as MainActivity).goMyInfo()
        }

        //독서 목표 관리 화면으로 이동하는 이미지 클릭 리스너 -> GoalManagementActivity 화면으로 이동
        binding.readingGoalNextClickView.setOnClickListener {
            (requireActivity() as MainActivity).goGoalManagement()
        }

        //체크리스트뷰 클릭 리스너 -> 아직 개발중임을 보여주는 토스트 메세지 띄우기
        binding.checklistView.setOnClickListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_checklist_not_develop),
                Toast.LENGTH_SHORT
            ).show()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainFragment", "onResume")

        getUser()
        getTotalPostCnt()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("MainFragment", "onDetach")
    }

    private fun getTotalPostCnt() {
        CoroutineScope(Dispatchers.Main).launch {
            postVm.getTotalPostCnt()
        }
    }

    private fun initAdapter() {
        checklistContentAdapter.setMyChecklistContentRVAdapterListener(object :
            ChecklistContentRVAdapter.MyChecklistContentRVAdapterListener {
            override fun addChecklist(position: Int, content: String) {
                createChecklist(position, content)
            }

            override fun updateChecklist(position: Int, toDoId: Long, content: String) {
                val checklist =
                    CheckListModel(toDoId = toDoId, content = content, isComplete = false)
                modifyChecklist(position, checklist)
            }

            override fun completeChecklist(position: Int, checklist: CheckListModel) {
                checklist.isComplete = true
                modifyChecklist(position, checklist)
            }

        })
    }

    private fun createChecklist(position: Int, content: String) {

        CoroutineScope(Dispatchers.Main).launch {
            val checklist = CheckListModel(content = content, isComplete = false)
            checklistVm.createChecklist(checklist)
        }
    }

    private fun getUser() {
        CoroutineScope(Dispatchers.Main).launch {
            mainVm.getUser()
        }
    }

    private fun modifyChecklist(position: Int, checklist: CheckListModel) {
        CoroutineScope(Dispatchers.Main).launch {
            checklistVm.updateChecklist(position, checklist)

            if (checklist.isComplete) {
                checklistContentAdapter.removeChecklist(position)
            }
        }
    }

    private fun setUI(user: UserModel) {
        binding.writeInitTitleTv.text = "${user.nickname}님,\n오늘도 힘차게 기록해봅시다."

        if (user.goal == "") {
            binding.readingGoalTv.text = "목표를 설정해주세요."
        } else {
            binding.readingGoalTv.text = user.goal
        }
    }

    private fun setTotalPostCntUI(it: Int) {
        binding.totalPagesTv.text = "total $it pages"
    }

    private fun observe() {
        checklistVm.unCompletedChecklists.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("MainFragment", "checklists observe!! -> $it")

            checklistContentAdapter.setChecklist(it)
            checklistContentAdapter.notifyDataSetChanged()

        })

        mainVm.user.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("MainFragment", "user observe!! -> $it")
            setUI(it)
        })

        postVm.totalCnt.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("MainFragment", "totalCnt observe!! -> $it")
            setTotalPostCntUI(it)
        })
    }
}