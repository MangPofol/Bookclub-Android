package com.mangpo.bookclub.view.write

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.mangpo.bookclub.databinding.FragmentWritingSettingBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.PostDetailModel
import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.view.LoadingDialogFragment
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class WritingSettingFragment(private val isUpdate: Boolean) : Fragment() {

    private lateinit var binding: FragmentWritingSettingBinding
    private lateinit var post: PostModel
    private lateinit var book: BookModel

    private val postVm: PostViewModel by sharedViewModel()
    private val bookVm: BookViewModel by sharedViewModel()
    private val loadingDialogFragment = LoadingDialogFragment()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("WritingSettingFragment", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("WritingSettingFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("WritingSettingFragment", "onCreateView")

        binding = FragmentWritingSettingBinding.inflate(inflater, container, false)

        observe()

        post = postVm.getPost()!!
        book = bookVm.getBook()!!

        //완료 버튼 클릭 리스너
        binding.completeBtn.setOnClickListener {
            (requireActivity() as MainActivity).hideKeyBord(requireView())
            loadingDialogFragment.show(requireActivity().supportFragmentManager, null)
            addRecordData() //기록 데이터 저장하는 함수 호출

            Log.d("WritingSettingFragment", "addRecordData -> $post")

            if (post.postImgLocations.isEmpty()) {    //이미지가 없는 경우
                if (post.bookId == null)
                    createBook()
                else if (isUpdate)
                    updatePost(post)
                else
                    createPost(post)
            } else if (post.postImgLocations.size == 1 && !post.postImgLocations[0].startsWith("https://")) {   //이미지가 한개 있으면
                uploadImg(post.postImgLocations[0])   //하나의 이미지를 업로드하는 함수 호출
            } else {    //이미지가 여러개 있으면
                uploadMultiImg(post.postImgLocations) //여러개의 이미지를 업로드하는 함수 호출
            }
        }

        binding.backIvView.setOnClickListener {
            parentFragmentManager.popBackStackImmediate()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("WritingSettingFragment", "onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("WritingSettingFragment", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("WritingSettingFragment", "onResume")

        initUI(post)
    }

    override fun onPause() {
        super.onPause()
        Log.d("WritingSettingFragment", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("WritingSettingFragment", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("WritingSettingFragment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("WritingSettingFragment", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("WritingSettingFragment", "onDetach")
    }

    private fun initUI(post: PostModel) {
        binding.readWhereEt.setText(post?.location)
        binding.readTimeEt.setText(post?.readTime)
        binding.linkTitleEt.setText(post?.hyperlinkTitle)
        binding.linkEt.setText(post?.hyperlink)
    }

    //책 등록 함수
    private fun createBook() {
        CoroutineScope(Dispatchers.Main).launch {
            val code = CoroutineScope(Dispatchers.Main).async {
                bookVm.createBook(bookVm.getBook()!!)
            }

            if (code.await() == 201) {  //책이 등록됐으면
                post.bookId = book.id    //post 의 bookId 저장

                if (isUpdate)
                    updatePost(post)
                else
                    createPost(post)  //기록 추가 함수 호출
            } else if (code.await() == 400) {
                Toast.makeText(requireContext(), "책을 선택해주세요.", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStackImmediate()
            } else {
                Toast.makeText(requireContext(), "책 등록 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStackImmediate()
            }
        }
    }

    //기록 추가 함수
    private fun createPost(post: PostModel) {
        Log.d("WritingSettingFragment", "createPost")
        CoroutineScope(Dispatchers.Main).launch {
            val postDetail = postVm.createPost(post)

            loadingDialogFragment.dismiss()

            if (postDetail != null) {
                Log.d("WritingSettingFragment", "CreatePost is success!\npostDetail: $postDetail")

                postVm.setPostDetail(postDetail)
                (requireParentFragment() as WriteFrameFragment).moveToPostDetail(
                    post.bookId,
                    book.name
                )

                bookVm.setBook(BookModel())
                postVm.setPost(PostModel())
                postVm.setImgUriList(listOf())
            } else {
                Log.d("WritingSettingFragment", "CreatePost is fail!")
                Toast.makeText(requireContext(), "게시글 업로드 중 오류 발생. 다시 시도해 주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun updatePost(post: PostModel) {
        Log.d("WritingSettingFragment", "updatePost")
        CoroutineScope(Dispatchers.Main).launch {
            val isUpdate = postVm.updatePost(postVm.getPostDetail()!!.id, post)

            loadingDialogFragment.dismiss()

            if (isUpdate) {
                Log.d("WritingSettingFragment", "updatePost -> Success")
                setPostDetail()
                (requireParentFragment() as WriteFrameFragment).moveToPostDetail(
                    post.bookId,
                    book.name
                )

                bookVm.setBook(BookModel())
                postVm.setPost(PostModel())
                postVm.setImgUriList(listOf())
            } else
                Log.d("WritingSettingFragment", "updatePost -> Fail")
        }
    }

    //하나의 이미지를 업로드하는 함수
    private fun uploadImg(imgPath: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val path = CoroutineScope(Dispatchers.IO).async {
                postVm.uploadImg(getAbsolutePathByBitmap(uriToBitmap(Uri.parse(imgPath))))
            }

            if (path.await() != null) {
                //post 의 postImgLocations 를 aws s3에 업로드한 주소로 저장.
                post.postImgLocations = listOf(path.await()!!)

                if (post.bookId == null)  //post 의 bookId 가 존재하지 않으면 책 등록부터 하기
                    createBook()
                else if (isUpdate)
                    updatePost(post)
                else
                    createPost(post)  //post 의 bookId 가 존재하면 기록 추가
            } else {
                Log.d("WritingSettingFragment", "uploadImg error!")

            }
        }
    }

    //여러개의 이미지를 업로드하는 함수
    private fun uploadMultiImg(imgPaths: List<String>) {

        val finalImgList = arrayListOf<String>()
        val imgList = arrayListOf<String>()

        for (path in imgPaths) {
            if (path.startsWith("https"))
                finalImgList.add(path)
            else
                imgList.add(getAbsolutePathByBitmap(uriToBitmap(Uri.parse(path))))
        }

        CoroutineScope(Dispatchers.Main).launch {
            if (imgList.size != 0) {
                val path = CoroutineScope(Dispatchers.IO).async {
                    postVm.uploadMultiImg(imgList)
                }

                if (path.await() != null)
                    finalImgList.addAll(path.await()!!)
                else
                    Log.d("WritingSettingFragment", "uploadMultiImg error!")
            }

            post.postImgLocations = finalImgList

            when {
                post.bookId == null -> createBook()    //post 의 bookId 가 존재하지 않으면 책 등록부터 하기
                isUpdate -> updatePost(post)
                else -> createPost(post)    //post 의 bookId 가 존재하면 기록 추가
            }
        }
    }

    //기록 데이터 저장하는 함수
    private fun addRecordData() {
        post.scope = "PRIVATE"

        if (binding.readWhereEt.text.isNotBlank())
            post.location = binding.readWhereEt.text.toString()
        else
            post.location = null

        if (binding.readTimeEt.text.isNotBlank())
            post.readTime = binding.readTimeEt.text.toString()
        else
            post.readTime = null

        if (binding.linkTitleEt.text.isNotBlank())
            post.hyperlinkTitle = binding.linkTitleEt.text.toString()
        else
            post.hyperlinkTitle = null

        if (binding.linkEt.text.isNotBlank())
            post.hyperlink = binding.linkEt.text.toString()
        else
            post.hyperlink = null

        postVm.setPost(post)
    }

    private fun uriToBitmap(uri: Uri): Bitmap {
        Log.d("WritingSettingFragment", "uriToBitmap -> $uri")
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(
                requireActivity().contentResolver,
                uri
            )
        } else {
            val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }

        return bitmap
    }

    private fun getAbsolutePathByBitmap(bitmap: Bitmap): String {
        val path =
            (requireContext().applicationInfo.dataDir + File.separator + System.currentTimeMillis())
        val file = File(path)
        var out: OutputStream? = null

        try {
            file.createNewFile()
            out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out)
        } finally {
            out?.close()
        }

        return file.absolutePath
    }

    private fun observe() {
        //BookViewModel book observer
        bookVm.book.observe(viewLifecycleOwner, Observer {
            book = it
        })

        postVm.post.observe(viewLifecycleOwner, Observer {
            post = it
        })
    }

    private fun setPostDetail() {
        val postDetail: PostDetailModel = postVm.getPostDetail()!!
        postDetail.scope = post.scope!!
        postDetail.isIncomplete = post.isIncomplete
        postDetail.title = post.title!!
        postDetail.content = post.content!!
        postDetail.location = post.location
        postDetail.readTime = post.readTime
        postDetail.hyperlinkTitle = post.hyperlinkTitle
        postDetail.hyperlink = post.hyperlink
        postDetail.postImgLocations = post.postImgLocations

        postVm.setPostDetail(postDetail)
    }

}