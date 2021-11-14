package com.mangpo.bookclub.view.write

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentRecordBinding
import com.mangpo.bookclub.view.adapter.RecordImgRVAdapter
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.PostViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.*


class RecordFragment : Fragment() {
    private lateinit var binding: FragmentRecordBinding
//    private lateinit var callback: OnBackPressedCallback

    private val recordImgRVAdapter: RecordImgRVAdapter = RecordImgRVAdapter()
    private val bookVm: BookViewModel by sharedViewModel()
    private val postVm: PostViewModel by sharedViewModel()
    private val cameraGalleryBottomSheet: CameraGalleryBottomSheetFragment = CameraGalleryBottomSheetFragment()

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

        if (bookVm.getSelectedBook()!=null) {
            binding.selectBookBtn.text = bookVm.getSelectedBook()!!.name
        } else {
            Log.d("RecordFragment", "selectedBook is null!")
        }

        //이미지 선택하기 클릭 리스너
        binding.addImgView.setOnClickListener {
            setPost()    //title이랑 content를 작성했으면 우선 postViewModel에 저장해놓기
            cameraGalleryBottomSheet.show((activity as MainActivity).supportFragmentManager, cameraGalleryBottomSheet.tag)
        }

        //책 선택 버튼을 누르면 SelectBookFragment로 이동
        binding.selectBookBtn.setOnClickListener {
            (activity as MainActivity).hideKeyBord(requireView())   //키보드 올라와 있으면 키보드 내리기
            setPost()    //title이랑 content를 작성했으면 우선 postViewModel에 저장해놓기
            (requireParentFragment() as WriteFragment).moveToSelectBook()
        }

        //선택한 이미지를 보여주는 recycler view 어댑터 설정
        binding.recordImgRv.adapter = recordImgRVAdapter
        //RecordImgRVAdapter MyItemClickListener 인터페이스의 removeItem 메서드 구현 -> PostViewModel의 imgs 라이브 데이터도 삭제된 후 데이터로 업데이트
        recordImgRVAdapter.setMyItemClickListener(object : RecordImgRVAdapter.MyItemClickListener {
            override fun removeItem(position: Int) {
                postVm.removeImg(position)
            }
        })

        //다음 버튼 클릭 리스너 -> 글 설정 프래그먼트로 이동하기
        binding.nextBtn.setOnClickListener {
            setPost()
            (requireParentFragment() as WriteFragment).moveToWritingSetting()
        }

        return binding.root

        //뒤로가기 콜백
        /*callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.selectedBookTv.visibility == View.INVISIBLE && !binding.memoRB.isChecked &&
                    !binding.topicRB.isChecked && postViewModel.imgUriList.value==null &&
                    binding.postTitleET.text.toString() == "" && binding.contentET.text.toString()=="") {  //기록 내용이 없으면 -> 앱 종료료
                    requireActivity().finish()
                } else    //기록 내용이 있으면 -> 모든 값 초기화
                    init()
            }
        }*/
        //requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        //selectedBook observer
        /*bookViewModel.selectedBook.observe(viewLifecycleOwner, Observer {
            if (it.name == "") { //빈값이면 책 선택 버튼에 "기록할 책을 선택하세요"
                binding.selectBookBtn.visibility = View.VISIBLE
                binding.selectedBookTv.visibility = View.INVISIBLE
                binding.selectBookBtn.text = getString(R.string.book_select)
            } else {    //값이 존재하면 책 선택 버튼에 추가된 책의 이름
                binding.selectBookBtn.visibility = View.INVISIBLE
                binding.selectedBookTv.visibility = View.VISIBLE
                binding.selectedBookTv.text = it.name
            }
        })*/

        //올리기 버튼을 클릭 리스너
        /*binding.nextBtn.setOnClickListener {
            //모든 필수 항목을 다 입력했는지 유효성 검사
            if (checkEmpty()) {
                try {
                    (requireActivity() as MainActivity).hideKeyBord(this.requireView()) //키보드 내리기

                    //recordScopeBottomSheet 콜백 함수
                    val recordScopeBottomSheet: RecordScopeBottomSheetFragment =
                        RecordScopeBottomSheetFragment {
                            if (it) {
                                when {
                                    //이미지 없을 때
                                    //postViewModel.imgUriList.value == null -> createPost()

                                    //이미지가 한 개일 때 -> test 성공
                                    *//*postViewModel.imgUriList.value!!.size==1 -> {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            try {
                                                val imgUrl = postViewModel.uploadImg(createCopyAndReturnRealPath(requireContext(), postViewModel.imgUriList.value!![0]))
                                                postViewModel.temporaryPost.value!!.imgLocation = imgUrl
                                                Log.d("Record-이미지 업로드", postViewModel.temporaryPost.value!!.imgLocation.toString())

                                                createPost()
                                            } catch (e: Exception) {
                                                Log.e("Record", "한 개의 이미지를 서버에 업로드 하는 과정 중 오류 발생 -> ${e.message}")
                                            }
                                        }
                                    }*//*

                                    //이미지가 여러개일 때 -> test 성공
                                    *//*postViewModel.imgUriList.value!!.size>1 -> {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            try {
                                                val paths: MutableList<String> = mutableListOf(
                                                    createCopyAndReturnRealPath(requireContext(), postViewModel.imgUriList.value!![0]),
                                                    createCopyAndReturnRealPath(requireContext(), postViewModel.imgUriList.value!![1]))

                                                postViewModel.uploadMultipleImg(paths)
                                            } catch (e: Exception) {
                                                Log.e("Record", "여러 개의 이미지를 서버에 업로드 하는 과정 중 오류 발생 -> \n ${e.message}")
                                            }
                                        }
                                    }*//*

                                    *//*else -> {
                                        createPost()
                                    }*//*

                                }

                            }
                        }

                    recordScopeBottomSheet.show(
                        (activity as MainActivity).supportFragmentManager,
                        recordScopeBottomSheet.tag
                    )
                } catch (e: Exception) {
                    Log.e("Record", "올리기 버튼 클릭 리스너 error -> ${e.message}")
                }
            }

        }*/

        //사진 추가이미지 버튼 클릭 리스너
//        binding.addImgView.setOnClickListener {
//            saveRecord()    //postViewModel temporaryPost에 지금까지 기록된 데이터 임시저장하기
//            //ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
//            requestGalleryPermission()  //갤러리로 이동
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("RecordFragment", "onViewCreated")

        binding.toolbar.setNavigationIcon(R.drawable.back_icon_small)  //navigation icon 설정
//        (activity as MainActivity).setDrawer(binding.toolbar)   //navigation drawer 등록
//        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_more_vert_36_black)  //navigation icon 설정
    }

    override fun onResume() {
        super.onResume()
        Log.d("RecordFragment", "onResume")

        bindTitleAndContent()   //책 선택 or 이미지 선택/촬영하기 전에 작성한 제목, 내용 불러오기

       /* if (postViewModel.temporaryPost.value==null)    //임시저장 기록 데이터 null 이면 초기화
            postViewModel.initTemtporaryPost()
        else
            //uiUpdate()

        if (postViewModel.imgUriList.value != null) {   //갤러리에서 이미지 선택하고 온 경우
            binding.imgCntTv.text = postViewModel.imgUriList.value!!.size.toString()
            recordImgAdapter.setData(postViewModel.imgUriList.value!!)
        } else  //선택한 이미지가 없으면 imgCnt를 0으로
            binding.imgCntTv.text = "0"*/
    }

    override fun onPause() {
        super.onPause()
        Log.d("RecordFragment", "onPause")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("RecordFragment", "onDetach")

//        callback.remove()
    }

    private fun observe() {
        //기록 이미지 observer
        postVm.imgs.observe(viewLifecycleOwner, Observer {
            Log.d("RecordFragment", "Observe!!")
            recordImgRVAdapter.setData(it)
        })
    }

    private fun setPost() {
        if (binding.postTitleET.text.isBlank())
            postVm.setTitle(null)
        else
            postVm.setTitle(binding.postTitleET.text.toString())

        if (binding.contentET.text.isBlank())
            postVm.setContent(null)
        else
            postVm.setContent(binding.contentET.text.toString())

        if (bookVm.getSelectedBook()==null)
            postVm.setBookId(null)
        else {
            postVm.setBookId(bookVm.getSelectedBook()!!.id)
        }

    }

    private fun bindTitleAndContent() {
        binding.postTitleET.setText(postVm.getTitle())
        binding.contentET.setText(postVm.getContent())
    }

    //기록하기 입력창 유효성 검사 함수
    /*private fun checkEmpty(): Boolean {
        if (binding.selectBookBtn.visibility == View.VISIBLE) {
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
    }*/

//    private fun requestGalleryPermission() {
//        (requireActivity() as MainActivity).galleryPermissionCallback.launch(permissions)
//    }

    //갤러리 이동했다가 다시 돌아왔을 때 기록 데이터가 계속 유지될 수 있도록
    /*private fun saveRecord() {
        val record: PostReqModel = PostReqModel()

        record.title = binding.postTitleET.text.toString()
        record.content = binding.contentET.text.toString()

        if (bookViewModel.selectedBook.value!=null)
            record.bookId = bookViewModel.selectedBook.value!!.id

        if (binding.topicRB.isChecked)
            record.type = "TOPIC"
        else if (binding.memoRB.isChecked)
            record.type = "MEMO"
        else
            record.type = ""

        postViewModel.setTemporaryPost(record)
    }*/

    private fun getAbsolutePathByBitmap(bitmap: Bitmap): String {
        val path = (requireContext().applicationInfo.dataDir + File.separator + System.currentTimeMillis())
        val file = File(path)
        var out: OutputStream? = null

        try {
            file.createNewFile()
            out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        } finally {
            out?.close()
        }

        return file.absolutePath
    }

    //이미지를 실제 경로로 변경하는 함수
    private fun createCopyAndReturnRealPath(context: Context, uri: Uri?): String {
        val contentResolver = context.contentResolver

        // 파일 경로를 만듬
        val filePath = (context.applicationInfo.dataDir + File.separator
                + System.currentTimeMillis())
        val file = File(filePath)
        try {
            // 매개변수로 받은 uri 를 통해  이미지에 필요한 데이터를 불러 들인다.
            val inputStream = contentResolver.openInputStream(uri!!)
            // 이미지 데이터를 다시 내보내면서 file 객체에  만들었던 경로를 이용한다.
            val outputStream: OutputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream!!.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
            outputStream.close()
            inputStream!!.close()
        } catch (ignore: IOException) {
            return ""
        }
        return file.absolutePath
    }

    //기록 데이터를 서버에 전송하기
    /*private fun createPost() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val createdPost: PostModel? = postViewModel.createPost(postViewModel.temporaryPost.value!!)

                if (createdPost == null) {
                    Toast.makeText(requireContext(), "기록 저장 중 오류 발생", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "기록 저장 완료!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("Record", "createPost ERROR!! -> ${e.message}")
            }
        }
    }*/

    //기록하기 화면 초기화(내용이 있으면 뒤로가기 버튼 눌렀을 때 모든 내용 삭제)
    /*private fun init() {
        binding.selectBookBtn.visibility = View.VISIBLE
        binding.selectedBookTv.visibility = View.INVISIBLE
        binding.memoTopicRG.clearCheck()
        binding.postTitleET.text = null
        binding.contentET.text = null
        binding.imgCntTv.text = "0"
        bookViewModel.clearSelectedBook()
        postViewModel.updateImgUriList(null)
        postViewModel.setTemporaryPost(null)
        recordImgAdapter.setData(null)
    }*/

    /*private fun uiUpdate() {
        val record = postViewModel.temporaryPost.value!!

        binding.postTitleET.setText(record.title)
        binding.contentET.setText(record.content)

        when (record.type) {
            "MEMO" -> binding.memoRB.isChecked = true
            "TOPIC" -> binding.topicRB.isChecked = true
        }
    }*/

}