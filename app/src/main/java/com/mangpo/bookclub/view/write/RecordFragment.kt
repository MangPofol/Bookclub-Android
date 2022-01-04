package com.mangpo.bookclub.view.write

import android.content.Context
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
import com.mangpo.bookclub.view.CameraGalleryBottomSheetFragment2
import com.mangpo.bookclub.view.LoadingDialogFragment
import com.mangpo.bookclub.view.adapter.RecordImgRVAdapter
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.PostViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RecordFragment(private var isUpdate: Boolean) : Fragment() {
    private lateinit var binding: FragmentRecordBinding
    private lateinit var cameraGalleryBottomSheet: CameraGalleryBottomSheetFragment2

    private val recordImgRVAdapter: RecordImgRVAdapter = RecordImgRVAdapter()
    private val bookVm: BookViewModel by sharedViewModel()
    private val postVm: PostViewModel by sharedViewModel()
    private var imgList: ArrayList<String> = arrayListOf()  //최종 이미지 리스트
    private val newImgList: ArrayList<String> = arrayListOf()  //새롭게 추가된 이미지 리스트(기록 수정하기에서 사용)
    private val delImgList: ArrayList<String> =
        arrayListOf()  //기존 imgList 에서 삭제된 이미지 리스트(기록 수정하기에서 사용)
    private val loadingDialogFragment = LoadingDialogFragment()

    private var post: PostModel = PostModel()   //기록되고 있는 post

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

        //선택한 이미지를 보여주는 recycler view 어댑터 설정
        binding.recordImgRv.adapter = recordImgRVAdapter

        //RecordImgRVAdapter MyItemClickListener 인터페이스의 removeItem 메서드 구현 -> PostViewModel의 imgs 라이브 데이터도 삭제된 후 데이터로 업데이트
        recordImgRVAdapter.setMyItemClickListener(object : RecordImgRVAdapter.MyItemClickListener {
            override fun removeItem(path: String) {
                if (isUpdate && !newImgList.contains(path)) {   //기록 수정하기 화면에서 기존 이미지 삭제
                    val idx = imgList.indexOf(path)
                    delImgList.add(imgList.removeAt(idx))
                    recordImgRVAdapter.setData(imgList)
                } else if (isUpdate && newImgList.contains(path)) {   //기록 수정하기 화면에서 새로 추가된 이미지 삭제
                    val idx = newImgList.indexOf(path)
                    deleteImg(path, idx)
                } else {    //기록 추가하기에서 삭제
                    val idx = imgList.indexOf(path)
                    deleteImg(path, idx)
                }
            }
        })

        //책 선택 버튼을 누르면 SelectBookFragment로 이동
        binding.selectBookBtn.setOnClickListener {
            setPost()    //지금까지 기록된 post 저장하기
            (activity as MainActivity).hideKeyBord(requireView())   //키보드 올라와 있으면 키보드 내리기
            (requireActivity() as MainActivity).moveToSelect()
        }

        //이미지 선택하기 클릭 리스너
        binding.addImgView.setOnClickListener {
            if (imgList.size == 4) {
                Toast.makeText(requireContext(), R.string.max_image_desc, Toast.LENGTH_SHORT)
                    .show()
            } else {
                setPost()    //지금까지 기록된 post 저장하기

                //카메라를 실행할지 갤러리를 실행할지 선택하는 bottom sheet dialog 객체 가져오기
                //선택할 수 있는 이미지는 최대 선택 가능한 이미지 수(4) - 지금까지 선택한 이미지 수(imgList.size)로 한다.
                cameraGalleryBottomSheet =
                    CameraGalleryBottomSheetFragment2.newInstance(4 - imgList.size) {
                        imgSelectCallback(it)
                    }
                //카메라 촬영인지 갤러리 선택인지 bottom dialog 띄우기
                cameraGalleryBottomSheet.show(
                    (activity as MainActivity).supportFragmentManager,
                    cameraGalleryBottomSheet.tag
                )
            }
        }

        //다음 버튼 클릭 리스너 -> 글 설정 프래그먼트로 이동하기
        binding.nextBtn.setOnClickListener {
            (activity as MainActivity).hideKeyBord(requireView())

            if (validate()) {   //필수 데이터를 다 기록했는지 유효성 검사
                setPost()   //다시 한 번 기록한 데이터 저장

                //WritingSettingFragment 로 이동
                if (delImgList.isNotEmpty())
                    (requireActivity() as MainActivity).moveToWritingSetting(isUpdate, delImgList)
                else
                    (requireActivity() as MainActivity).moveToWritingSetting(isUpdate, null)
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

    //카메라 촬영 or 갤러리에서 사진 선택 후 콜백 함수
    private fun imgSelectCallback(paths: List<String>) {
        if (paths.size == 1)
            uploadImg(paths[0])
        else
            uploadMultiImg(paths as ArrayList<String>)
    }

    private fun setPost() {
        post.bookId = bookVm.getBook()?.id
        post.isIncomplete = false
        post.postImgLocations = imgList
        post.title = binding.postTitleET.text.toString()
        post.content = binding.contentET.text.toString()

        postVm.setPost(post)
    }

    //기록 데이터 유효성 검사 함수
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

    private fun setUpdateUI(post: PostModel) {
        binding.selectBookBtn.isEnabled = !isUpdate

        binding.postTitleET.setText(post.title)
        binding.contentET.setText(post.content)

        if (post.postImgLocations != null) {
            recordImgRVAdapter.setData(post.postImgLocations)
        }
    }

    //하나의 이미지를 업로드하는 함수
    private fun uploadImg(imgPath: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val path = postVm.uploadImg(imgPath)

            postVm.setImgLoading(0)

            when {
                path == null -> Toast.makeText(
                    requireContext(),
                    "이미지 업로드 중 오류 발생. 다시 시도해 주세요.",
                    Toast.LENGTH_SHORT
                ).show()
                isUpdate -> {
                    newImgList.add(path)
                    imgList.add(path)
                }
                else -> imgList.add(path)
            }

            recordImgRVAdapter.setData(imgList)
        }
    }

    //여러개의 이미지를 업로드하는 함수
    private fun uploadMultiImg(imgPaths: ArrayList<String>) {
        CoroutineScope(Dispatchers.Main).launch {
            val paths = postVm.uploadMultiImg(imgPaths)

            postVm.setImgLoading(0)

            when {
                paths == null -> Toast.makeText(
                    requireContext(),
                    "이미지 업로드 중 오류 발생. 다시 시도해 주세요.",
                    Toast.LENGTH_SHORT
                ).show()
                isUpdate -> {
                    newImgList.addAll(paths)
                    imgList.addAll(paths)
                }
                else -> imgList.addAll(paths)
            }

            recordImgRVAdapter.setData(imgList)
        }
    }

    //단일 이미지 삭제
    private fun deleteImg(path: String, idx: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val code = postVm.deleteImg(path)

            if (code == 204 && isUpdate) {    //기록 수정하기에서 이미지 삭제
                newImgList.remove(path)
                imgList.remove(path)
                recordImgRVAdapter.setData(imgList)
            } else if (code == 204 && !isUpdate) {    //기록 추가하기에서 이미지 삭제
                imgList.remove(path)
                recordImgRVAdapter.setData(imgList)
            } else {
                Toast.makeText(requireContext(), "이미지 삭제 중 오류 발생. 다시 시도해 주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun observe() {
        //BookViewModel book observer
        bookVm.book.observe(viewLifecycleOwner, Observer {
            if (it.name.isBlank())    //book.name 이 비어 있으면 selectedBtn.text = 기록할 책을 선택해주세요.
                binding.selectBookBtn.text = getString(R.string.book_select)
            else {  //book.id 가 null 이 아니면
                post.bookId = it.id
                binding.selectBookBtn.text = it.name
            }
        })

        postVm.post.observe(viewLifecycleOwner, Observer {
            post = it
            setUpdateUI(it)

            if (isUpdate)
                imgList = post.postImgLocations as ArrayList<String>
        })

        postVm.imgLoading.observe(viewLifecycleOwner, Observer {
            Log.d("RecordFragment", "imgLoading Observe!! -> $it")
            if (it == 1)
                loadingDialogFragment.show((activity as MainActivity).supportFragmentManager, null)
            else {
                loadingDialogFragment.dismiss()
            }
        })
    }
}