package com.mangpo.bookclub.view.write

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentRecordBinding
import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.view.adapter.RecordImgRVAdapter
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.PostViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RecordFragment(private val isUpdate: Boolean) : Fragment() {
    private lateinit var binding: FragmentRecordBinding

    private val recordImgRVAdapter: RecordImgRVAdapter = RecordImgRVAdapter()
    private val bookVm: BookViewModel by sharedViewModel()
    private val postVm: PostViewModel by sharedViewModel()
    private val cameraGalleryBottomSheet: CameraGalleryBottomSheetFragment =
        CameraGalleryBottomSheetFragment()

    private var post: PostModel = PostModel()
    //private var book: BookModel = BookModel()
    private var imgList: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("RecordFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordBinding.inflate(inflater, container, false)
        Log.d("RecordFragment", "onCreateView")

        observe()

        //책 선택 버튼을 누르면 SelectBookFragment로 이동
        binding.selectBookBtn.setOnClickListener {
            setPost()    //지금까지 기록된 post 저장하기
            (activity as MainActivity).hideKeyBord(requireView())   //키보드 올라와 있으면 키보드 내리기
            (requireParentFragment() as WriteFrameFragment).moveToSelect()
        }

        //이미지 선택하기 클릭 리스너
        binding.addImgView.setOnClickListener {
            if (checkPictureCnt()==4) {
                Toast.makeText(requireContext(), "사진은 최대 4장까지 첨부할 수 있습니다.", Toast.LENGTH_SHORT).show()
            } else {
                setPost()    //지금까지 기록된 post 저장하기

                //카메라 촬영인지 갤러리 선택인지 bottom dialog 띄우기
                cameraGalleryBottomSheet.show(
                    (activity as MainActivity).supportFragmentManager,
                    cameraGalleryBottomSheet.tag
                )
            }
        }

        //선택한 이미지를 보여주는 recycler view 어댑터 설정
        binding.recordImgRv.adapter = recordImgRVAdapter

        //RecordImgRVAdapter MyItemClickListener 인터페이스의 removeItem 메서드 구현 -> PostViewModel의 imgs 라이브 데이터도 삭제된 후 데이터로 업데이트
        recordImgRVAdapter.setMyItemClickListener(object : RecordImgRVAdapter.MyItemClickListener {
            override fun removeItem(position: Int) {
                val imgUriList = postVm.getImgUriList() as ArrayList
                imgUriList.removeAt(position)
                postVm.setImgUriList(imgUriList)
            }
        })

        //다음 버튼 클릭 리스너 -> 글 설정 프래그먼트로 이동하기
        binding.nextBtn.setOnClickListener {
            (activity as MainActivity).hideKeyBord(requireView())

            if (validate()) {   //필수 데이터를 다 기록했는지 유효성 검사
                setPost()   //다시 한 번 기록한 데이터 저장
                Log.d("RecordFragment", "next click! -> post: $post")
                (requireParentFragment() as WriteFrameFragment).moveToWritingSetting(isUpdate)  //WritingSettingFragment 화면으로 이동하는 함수 호출
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("RecordFragment", "onViewCreated")
    }

    override fun onResume() {
        super.onResume()
        Log.d("RecordFragment", "onResume")

        Log.d("RecordFragment", "imgList: $imgList")
    }

    override fun onPause() {
        super.onPause()
        Log.d("RecordFragment", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("RecordFragment", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("RecordFragment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("RecordFragment", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("RecordFragment", "onDetach")
    }

    private fun observe() {
        //BookViewModel book observer
        bookVm.book.observe(viewLifecycleOwner, Observer {
            Log.d("RecordFragment", "book observe!! -> book: $it")
            if (it.name.isBlank())    //book.name 이 비어 있으면 selectedBtn.text = 기록할 책을 선택해주세요.
                binding.selectBookBtn.text = getString(R.string.book_select)
            else {  //book.id 가 null 이 아니면
                post.bookId = it.id
                binding.selectBookBtn.text = it.name
            }
            Log.d("RecordFragment", "book observe!! -> post: $post")
        })

        //기록 이미지 observer
        postVm.imgUriList.observe(viewLifecycleOwner, Observer {
            Log.d("RecordFragment", "imgUriList observe -> $it")
            recordImgRVAdapter.setData(it)
            uriToString(it)
            Log.d("RecordFragment", "imgUriList observe -> imgList: $imgList")
        })

        postVm.post.observe(viewLifecycleOwner, Observer {
            post = it
            setUpdateUI(it)
        })
    }

    private fun uriToString(imgUriList: List<Uri>) {
        imgList.clear()

        for (imgUri in imgUriList) {
            imgList.add(imgUri.toString())
        }
    }

    private fun setPost() {
        post.bookId = bookVm.getBook()?.id
        post.isIncomplete = false
        post.postImgLocations = imgList
        post.title = binding.postTitleET.text.toString()
        post.content = binding.contentET.text.toString()

        postVm.setPost(post)
    }

    private fun validate(): Boolean {
        when {
            binding.selectBookBtn.text.toString() == getString(R.string.book_select) -> {
                Toast.makeText(requireContext(), "책을 선택해주세요.", Toast.LENGTH_SHORT).show()

                return false
            }
            binding.postTitleET.text.isBlank() -> {
                Toast.makeText(requireContext(), "메모 제목을 입력해주세요.", Toast.LENGTH_SHORT).show()

                return false
            }
            binding.contentET.text.isBlank() -> {
                Toast.makeText(requireContext(), "메모 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()

                return false
            }
            else -> return true
        }
    }

    //현재 저장된 사진이 몇 장인지 확인하는 함수
    private fun checkPictureCnt(): Int? = postVm.getImgUriList()?.size

    private fun setUpdateUI(post: PostModel) {
        binding.selectBookBtn.isEnabled = !isUpdate

        binding.postTitleET.setText(post.title)
        binding.contentET.setText(post.content)

        if (post.postImgLocations!=null) {
            val uriList: ArrayList<Uri> = ArrayList()
            for (postImgLocation in post!!.postImgLocations) {
                uriList.add(Uri.parse(postImgLocation))
            }
            recordImgRVAdapter.setData(uriList)
            postVm.setImgUriList(uriList)
        }
    }
}