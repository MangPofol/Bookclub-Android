package com.mangpo.bookclub.view.write

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentRecordBinding
import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.view.MainActivity
import com.mangpo.bookclub.view.adapter.OnItemClick
import com.mangpo.bookclub.view.adapter.RecordImgAdapter
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel

class RecordFragment : Fragment(), OnItemClick {
    private lateinit var binding: FragmentRecordBinding
    private lateinit var callback: OnBackPressedCallback

    private val recordImgAdapter: RecordImgAdapter = RecordImgAdapter(this)
    private val bookViewModel: BookViewModel by activityViewModels<BookViewModel>()
    private val postViewModel: PostViewModel by activityViewModels<PostViewModel>()
    private val permissions: Array<String> = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("Record", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordBinding.inflate(inflater, container, false)
        Log.e("Record", "onCreateView")

        //뒤로가기 콜백
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //책이 선택돼 있는 상태면
                if (!binding.selectBookBtn.text.equals(getString(R.string.book_select)))
                //책 제목 지우고 기록할 책을 선택해주세요 로 바꾸기
                    binding.selectBookBtn.text = getString(R.string.book_select)
                else    //책이 선택돼 있는 상태가 아니면
                    requireActivity().finish()  //앱 종료
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val newPost: PostModel = PostModel()

        //selectedBook observer
        bookViewModel.selectedBook.observe(viewLifecycleOwner, Observer {
            if (it.name == "") { //빈값이면 책 선택 버튼에 "기록할 책을 선택하세요"
                binding.selectBookBtn.text = getString(R.string.book_select)
            } else {    //값이 존재하면 책 선택 버튼에 추가된 책의 이름
                binding.selectBookBtn.text = it.name
            }
        })

        binding.selectBookBtn.setOnClickListener {  //책 선택 버튼을 누르면 SelectBookFragment로 이동
            (requireParentFragment() as WriteFragment).moveToSelectBook()
        }

        //메모, 토픽 선택하면 newPost type 변경
        binding.memoTopicRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.memoRB.id -> newPost.type = "MEMO"
                binding.memoTopicRG.id -> newPost.type = "TOPIC"
            }
        }

        binding.postTV.setOnClickListener { //올리기 버튼을 클릭 리스너
            //모든 필수 항목을 다 입력했는지 유효성 검사
            if (checkEmpty()) {
                (requireActivity() as MainActivity).hideKeyBord(this.requireView())
                Toast.makeText(context, "필수 입력창을 모두 입력했습니다!", Toast.LENGTH_SHORT).show()
            }

        }

        binding.addImgView.setOnClickListener {
            requestGalleryPermission()
        }

        //선택한 이미지를 보여주는 recycler view 어댑터 설정
        binding.recordImgRv.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.recordImgRv.adapter = recordImgAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("Record", "onViewCreated")

        (activity as MainActivity).setDrawer(binding.toolbar)   //navigation drawer 등록
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_more_vert_36_black)  //navigation icon 설정
    }

    override fun onResume() {
        super.onResume()
        Log.e("Record", "onResume")

        if (postViewModel.imgUriList.value!=null) {
            binding.imgCntTv.text = postViewModel.imgUriList.value!!.size.toString()
            recordImgAdapter.setData(postViewModel.imgUriList.value!!)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.e("Record", "onPause")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("Record", "onDetach")

        callback.remove()
    }

    override fun onClick(size: Int) {
        binding.imgCntTv.text = size.toString()
    }

    //기록하기 입력창 유효성 검사 함수
    private fun checkEmpty(): Boolean {
        if (binding.selectBookBtn.text.equals(getString(R.string.book_select))) {
            Toast.makeText(
                context,
                "책을 선택해주세요",
                Toast.LENGTH_SHORT
            ).show()

            return false
        } else if (binding.memoTopicRG.checkedRadioButtonId == -1) {
            Toast.makeText(
                context,
                "메모/토픽 중 선택해주세요",
                Toast.LENGTH_SHORT
            ).show()

            return false
        } else if (binding.postTitleET.text.isEmpty()) {
            Toast.makeText(
                context,
                "제목을 입력해주세요",
                Toast.LENGTH_SHORT
            ).show()

            return false
        } else if (binding.contentET.text.isEmpty()) {
            Toast.makeText(
                context,
                "내용을 입력해주세요",
                Toast.LENGTH_SHORT
            ).show()

            return false
        } else {
            return true
        }
    }

    private fun requestGalleryPermission() {
        (requireActivity() as MainActivity).galleryPermissionCallback.launch(permissions)
    }

}